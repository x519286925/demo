package com.test.demo.controller;

import com.google.gson.Gson;
import com.lly835.bestpay.rest.type.Get;
import com.test.demo.VO.InformationListVO;
import com.test.demo.config.UrlConfig;
import com.test.demo.constant.CookiesConstant;
import com.test.demo.constant.RedisConstant;
import com.test.demo.converter.InformationToVoConverter;
import com.test.demo.entity.ScanLogin;
import com.test.demo.entity.User;
import com.test.demo.entity.monEntity.Information;
import com.test.demo.service.*;
import com.test.demo.util.FileUtil;
import com.test.demo.utils.CookiesUtil;
import com.test.demo.utils.KeyUtil;
import com.test.demo.utils.PasswordUtil;
import com.test.demo.websocket.WebSocket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by yhn on 2017/10/31.
 */
@RestController
@Slf4j
public class RditNewsController {
    @Autowired
    private UrlConfig urlConfig;
    @Autowired
    private UploadFileService uploadFileService;
    @Autowired
    private UserService userService;
    @Autowired
    private ScanLoginService scanLoginService;
    @Autowired
    private WebSocket webSocket;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private PushMessageService pushMessageService;
    @Autowired
    private MonNewsService monNewsService;

    @GetMapping("/index")     //发布页面
    public ModelAndView index(
                        @RequestParam(value = "newsId",defaultValue = "0")String id,
                        Map<String, Object> map) {
//        //-----以下操作为未登陆默认设置-----
//        attributes.getRequest().getSession().setAttribute("useridSession",userid);
//        System.out.println("useridSession："+userid);
//        //-----------------------------
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String scanuserid = "undefind";
        try {
             scanuserid = (String) attributes.getRequest().getSession().getAttribute("userid");  //openid
            log.info("用户的openid=" + scanuserid);
            if (scanuserid == null) {
                map.put("scanuserid", "undefind");
            } else {
                map.put("scanuserid", scanuserid);
            }
        } catch (Exception e) {
            map.put("scanuserid", "undefind");
        }
        map.put("projectUrl", urlConfig.getUrl() + urlConfig.getContextPath());
        Information nullInformation = new Information();
//        Gson gson = new Gson();
//        String nullInformationJson = gson.toJson(nullInformation);
//        log.info("已转json格式：nullInformationJson={}",nullInformationJson);
        map.put("information",nullInformation);   //设置为未定义;
        if(!id.equals("0")){   //属于编辑某个文章
            Information information = monNewsService.findById(id);
            User user = userService.findOne(scanuserid);
            if(user==null){
                map.put("msg","用户不存在,稍后自动跳转首页");
                map.put("url","/demo/index");
                log.error("/index非法操作：操作他人的文章");
                return new ModelAndView("common/error2");
            }

            if(!information.getUserId().equals(user.getId())){
                map.put("msg","禁止操作他人的文章页面！");
                map.put("url","/demo/index");
                log.error("/index非法操作：操作他人的文章");
                return new ModelAndView("common/error2");
            }
            map.put("information",information);
        }
        return new ModelAndView("page/index", map);
    }

    //处理文件上传
    @PostMapping("/testuploadimg")
    public String uploadImg(@RequestParam("file") MultipartFile file,
                            HttpServletRequest request) {
        String fileType = file.getContentType().split("/")[1];
        String fileName = KeyUtil.genUniqueKey() + "." + fileType;
        String result = uploadFileService.uploadFile(file, request, "image", "imgUpload", fileName, urlConfig.getUrl());
        log.info("【上传文件结果】,result={}", result);
        return result;
    }

    @GetMapping("getCodeImgUrl")      //异步得到二维码
    public synchronized String getCodeImgUrl() {   //同步
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String  webSokcetSessionId = (String)attributes.getRequest().getSession().getAttribute("webSokcetSessionId");
        log.info("sessionId={}" ,webSokcetSessionId);
        String returnUrl = urlConfig.getUrl() + urlConfig.getContextPath()
                + "/wechat/authorize?returnUrl=" + urlConfig.getUrl() + urlConfig.getContextPath() + "/wechatLogin?websocketSessionId=" +webSokcetSessionId; //未写完
        String userid =(String)attributes.getRequest().getSession().getAttribute("userid");
        if(userid!=null){
            log.warn("登陆中：userid={}"+userid);
            return "logined";
        }
        log.warn("userid："+userid);
        String qcodeImg = userService.qcodeImgUrl(returnUrl, attributes.getRequest());   //二维码路径
        //记录二维码事件
        ScanLogin scanLogin = new ScanLogin();
        scanLogin.setId(webSokcetSessionId);
        scanLogin.setCreateTime(new Date());
        scanLogin.setLoginStatus(0);  //是否登陆成功过
        scanLogin.setQcodeImgUrl(qcodeImg);
        scanLogin.setOpenid(null);  //无人登陆过
        scanLogin.setStatus(0);  //刚开始是未被扫
        scanLoginService.save(scanLogin);
        //----------------
        return qcodeImg;
    }

    @GetMapping("/wechatLogin")      //手机登录页面
    public ModelAndView login(@RequestParam("websocketSessionId") String websocketSessionId,
                              @RequestParam("openid") String openid,
                              Map<String, Object> map) {
        ScanLogin scanLogin = scanLoginService.findById(websocketSessionId);
        if (scanLogin == null) {
            map.put("msg", "警告:你在进行非法操作！");
            log.error("手机登录页面报错：openid={}",openid);
            return new ModelAndView("common/error", map);
        }
        if (scanLogin.getStatus() == 0) {  //表示没被扫过
            scanLogin.setStatus(1);
            scanLoginService.save(scanLogin);
            webSocket.sendMessage("scansuccess", websocketSessionId);
        } else {
            map.put("msg", "该二维码已经被其他人扫过请重新刷新页面");
            return new ModelAndView("common/error", map);
        }
        map.put("openid", openid);
        map.put("websocketSessionId", websocketSessionId);
        map.put("projectUrl", urlConfig.getUrl() + urlConfig.getContextPath());
        return new ModelAndView("page/scanLogin", map);
    }

    @GetMapping("/webQcodeLogin")      //登陆
    public String webQcodeLogin(@RequestParam("websocketSessionId") String websocketSessionId,
                                @RequestParam("openid") String openid,
                                @RequestParam("actions") String action,
                                Map<String, Object> map) {
        ScanLogin scanLogin = scanLoginService.findById(websocketSessionId);
        if (scanLogin == null) {
            return "警告:你在进行非法操作！";
        }
        if (action.equals("1")) {   //表示成功授权登陆
            scanLogin.setLoginStatus(1);//成功登陆
            scanLogin.setOpenid(openid);
            scanLoginService.save(scanLogin);
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            String scannuserid = (String) attributes.getRequest().getSession().getAttribute("userid");  //拿到扫描用户openid
            User user = userService.findOne(scannuserid);
            if (user == null) {   //无用户
                webSocket.sendMessage("fail",websocketSessionId);
                return "fail";
            }
            String userIdSha1 = PasswordUtil.getSha1(user.getId());
            //发送标签，还有websocketSessionId还有userid;
            webSocket.sendMessage("successLogin:" + scannuserid + "|" + userIdSha1, websocketSessionId);  //发送给电脑端
            pushMessageService.successSign(scannuserid);
            return "success";   //回调给web手机端
        } else {    //否则一律视为取消
            scanLogin.setLoginStatus(0);//表示未成功登陆
            scanLogin.setOpenid(openid);
            scanLoginService.save(scanLogin);
            webSocket.sendMessage("fail", websocketSessionId);
            return "fail";
        }

    }

    @PostMapping("/setSessionId")   //也是最后的校验
    public String setSessionId(@RequestParam("openid") String twoData) {     //web拿着openid设置session---->这里其实有两个参数：第一个是openid，第二个是加密的userid
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (twoData != null && twoData.length() > 0) {   //表示不为空
            log.warn("web最后握手【还不代表成功】:" + twoData);
            String openid = twoData.split("\\|")[0];
            String fromUseridSha1 = twoData.split("\\|")[1];   //来自手机端的userid加密
            User user = userService.findOne(openid);
            if (user == null) {
                return "fail";
            }
            String toUseridSha1 = PasswordUtil.getSha1(user.getId());   //来自openid查询的加密的userid
            if (toUseridSha1.equals(fromUseridSha1)) {    //符合条件
                log.info("【握手成功,两组非对称加密匹配成功】phone={},web={}", fromUseridSha1, toUseridSha1);
                attributes.getRequest().getSession().setMaxInactiveInterval(60 * 60 * 2);  //两小时过期
                attributes.getRequest().getSession().setAttribute("userid", openid);
                return "success";
            } else {
                log.error("【握手失败,两组非对称加密匹配成功】phone={},web={}", fromUseridSha1, toUseridSha1);
                return "fail";
            }
        }
        log.error("【握手失败,无传参】");
        return "fail";
    }


    @PostMapping("/mynews")
    public List<InformationListVO> getMyNews() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String openid = (String) attributes.getRequest().getSession().getAttribute("userid");
        User user = userService.findOne(openid);
        if (user == null) {
            return null;
        }
        List<Information> informationList = monNewsService.findByUserId(user.getId());
        List<InformationListVO> informationListVOList = InformationToVoConverter.convert(informationList);
        log.info("【我的文章列表】,informationListVOList={}", informationListVOList);
        return informationListVOList;
    }
}
