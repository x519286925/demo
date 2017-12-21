package com.test.demo.controller;

import com.test.demo.config.UrlConfig;
import com.test.demo.constant.CookiesConstant;
import com.test.demo.constant.RedisConstant;
import com.test.demo.entity.Manager;
import com.test.demo.entity.User;
import com.test.demo.enums.PowerEnum;
import com.test.demo.service.ManagerService;
import com.test.demo.service.UserService;
import com.test.demo.utils.CookiesUtil;
import com.test.demo.utils.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created by yhn on 2017/8/4.
 */
@Controller
@RequestMapping("/wechat")
@Slf4j
public class WechatController {
    @Autowired
    private WxMpService wxMpService; //已配置完成
    @Autowired
    private UserService userService;
    @Autowired
    private UrlConfig urlConfig;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private ManagerService managerService;
    @GetMapping("/authorize")
    public String authorize(@RequestParam("returnUrl") String returnUrl){
        //1.配置
        //2.调用方法
        String url = urlConfig.getUrl()+urlConfig.getContextPath()+"/wechat/userInfo";
        String redirectUrl =  wxMpService.oauth2buildAuthorizationUrl(url, WxConsts.OAUTH2_SCOPE_USER_INFO, URLEncoder.encode(returnUrl));
//        log.info("【微信网页授权】获取code，result={}",redirectUrl);
        return "redirect:"+redirectUrl;
    }
    @GetMapping("/userInfo")
    public String userInfo(@RequestParam("code") String code,
                           @RequestParam("state") String returnUrl){
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = new WxMpOAuth2AccessToken();
        WxMpUser wxMpUser = null;
            try{
                wxMpOAuth2AccessToken =  wxMpService.oauth2getAccessToken(code);
                wxMpUser =  wxMpService.oauth2getUserInfo(wxMpOAuth2AccessToken,null);
            }catch (WxErrorException e){
                log.info("【微信网页授权】{}",e);
                e.printStackTrace();
            }
         String openId = wxMpOAuth2AccessToken.getOpenId();
        //存储用户信息
        User userInfo = userService.findOne(openId);
        if(userInfo==null){
            User user = new User();
            user.setHeadImage(wxMpUser.getHeadImgUrl());
            user.setOpenId(openId);
            user.setCreateTime(new Date());
            user.setUpdateTime(new Date());
            user.setStatus(0);      //状态 默认0为正常状态，1为拉黑状态
            user.setPublisher(0);   //默认不是发布者
            user.setId(KeyUtil.genUniqueKey());
            user.setNickName("user_" + KeyUtil.genUniqueKey().substring(6));
            userService.save(user);    //保存用户信息
            Manager manager = new Manager();
            manager.setId(KeyUtil.genUniqueKey());
            manager.setOpenid(openId);
            manager.setPower(String.valueOf(PowerEnum.USER.getCode()));
            managerService.save(manager); //保存用户角色
        }
//        //设置token至redis
//        String token = UUID.randomUUID().toString();
//        Integer expire = RedisConstant.EXPIRE;
//        try {
//            redisTemplate.opsForValue().set(String.format(RedisConstant.TOKEN_PREFIX, token), openId, expire, TimeUnit.SECONDS);
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//        //设置token至cookies
          ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        HttpServletResponse response =  attributes.getResponse();
//        CookiesUtil.set(response, CookiesConstant.TOKEN,token,1800);
        //设置session
        attributes.getRequest().getSession().setAttribute("userid",openId);
        if(returnUrl.indexOf("?")!=-1){  //包含问号,说明回调地址带参
            return "redirect:" +returnUrl+"&openid="+openId;
        }
         return "redirect:" +returnUrl+"?openid="+openId+"&headImage="+wxMpUser.getHeadImgUrl();
    }
}
