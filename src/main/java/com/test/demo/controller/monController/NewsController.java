package com.test.demo.controller.monController;
import com.test.demo.config.UrlConfig;
import com.test.demo.entity.User;
import com.test.demo.entity.monEntity.Information;
import com.test.demo.enums.NewStatus;
import com.test.demo.form.NewsForm;
import com.test.demo.service.MonNewsService;
import com.test.demo.service.UploadFileService;
import com.test.demo.service.UserService;
import com.test.demo.utils.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yhn on 2017/11/1.
 */
@RestController
@RequestMapping("/news")
@Slf4j
public class NewsController {
    @Autowired
    private MonNewsService monNewsService;
    @Autowired
    private UserService userService;
    @Autowired
    private UrlConfig urlConfig;
    @PostMapping("/save")
    public String saveNews(@Valid NewsForm newsForm, BindingResult bindingResult,
                            @RequestParam(value = "newsId",defaultValue = "0") String id){   //id为0则表示新建文章。
        log.info("【后端是否拿到数据】,id={},title={},content={},photo={},type={},description={}",
                id,newsForm.getTitle(),newsForm.getContent(),newsForm.getPhoto(),newsForm.getType(),newsForm.getDescription());
        if(bindingResult.hasErrors()){
            log.error("【错误提交文章：】msg={}",bindingResult.getFieldError().getDefaultMessage());
            return bindingResult.getFieldError().getDefaultMessage();
        }
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String scanuserid = (String)attributes.getRequest().getSession().getAttribute("userid");   //这是openid
        if(scanuserid==null){
            return "很抱歉提醒您：您必须重新刷新页面进行授权登陆";
        }
        if(scanuserid.equals("undefind")){
            return "很抱歉提醒您：您必须重新刷新页面进行授权登陆";
        }
        log.info("【发表文章】，openid={}",scanuserid);
        User user =  userService.findOne(scanuserid);
        if(user==null){
            return "很抱歉提醒您：您必须重新刷新页面进行授权登陆";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
        if(id.equals("0")){   //新建
            Information monNews = new Information();
            BeanUtils.copyProperties(newsForm,monNews);
            monNews.setCreateTime(sdf.format(new Date()));
            monNews.setUpdateTime(sdf.format(new Date()));
            monNews.setDisplay(NewStatus.WANT_ALLOW.getCode());   //未给予显示
            monNews.setUserId(user.getId());
            monNews.setReadingQuantity(1);
            monNews.setId(KeyUtil.genUniqueKey());
            monNewsService.save(monNews);
            return "success:"+monNews.getId();
        }
        else{  //编辑  [应该会自己修改吧]
           Information informationEdit = monNewsService.findById(id);
            if(!informationEdit.getUserId().equals(user.getId())){
                log.error("非法操作：操作他人的文章");
                return "您这是非法操作：操作他人文章可是不好的行为";
            }
            BeanUtils.copyProperties(newsForm,informationEdit);
            informationEdit.setUpdateTime(sdf.format(new Date()));
            informationEdit.setDisplay(NewStatus.WANT_ALLOW.getCode());
            monNewsService.save(informationEdit);
            return "success:"+informationEdit.getId();
        }


    }
    @PostMapping("/displayforPhone")   //手机预览
    public String displayforPhone(@Valid NewsForm newsForm, BindingResult bindingResult,
                           @RequestParam(value = "newsId",defaultValue = "0") String id)throws Exception {   //id为0则表示新建文章。
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String result = "";
        String newsId = "";
        if("0".equals(id)){   //属于新建文章，但由于要预览。先认为是保存。默认给id让其新建。
             newsId = KeyUtil.genUniqueKey();
             result =  saveNews(newsForm,bindingResult,newsId);
        }
        else{       //编辑文章
             newsId = id;
             result =  saveNews(newsForm,bindingResult,newsId);
        }
        String returnUrl =urlConfig.getWxPhone()+"/information/content?id="+newsId;

        if(result.indexOf("success")!=-1){  //成功上传
            log.info("newsId ={}",newsId);
            Information information =  monNewsService.findById(newsId);  //查找文章
            log.info("【二维码】文章详情。 information={}",information);
            String qcode = information.getDisplayForPhone();  //查看是否已存在二维码;
            log.info("qcode={}",qcode);
            if(!StringUtils.isEmpty(qcode)){     //得到已经得到二维码
                try{
                    URL url = new URL(qcode);
                    InputStream in = url.openStream();   //正常说明可链接
                    return qcode+"|"+newsId;
                }catch(Exception e){
                    String imgUrl = userService.qcodeImgUrl(returnUrl, attributes.getRequest());
                    information.setDisplayForPhone(imgUrl);
                    log.warn("文件不存在,保存操作");
                    monNewsService.save(information);  //保存预览的二维码，保证不重复保存占用多余的内存
                    log.info("预览这里=" + imgUrl + "|" + newsId);
                    return imgUrl + "|" + newsId;
                }
            }
            else {
                String imgUrl = userService.qcodeImgUrl(returnUrl, attributes.getRequest());
                information.setDisplayForPhone(imgUrl);
                log.info("二维码不存在,保存操作");
                monNewsService.save(information);  //保存预览的二维码，保证不重复保存占用多余的内存
                log.info("预览这里=" + imgUrl + "|" + newsId);
                return imgUrl + "|" + newsId;
            }
        }
        else{
            return result;
        }
    }
}
