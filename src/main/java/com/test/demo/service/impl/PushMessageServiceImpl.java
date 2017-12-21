package com.test.demo.service.impl;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.test.demo.service.PushMessageService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by yhn on 2017/9/6.
 */
@Service
@Scope("prototype")
@Slf4j
public class PushMessageServiceImpl implements PushMessageService {
    @Autowired
    private WxMpService wxMpService;

    @Override
    public void newsResult(String openid,String result,String title) {
        WxMpTemplateMessage templateMessage = new WxMpTemplateMessage();
        templateMessage.setTemplateId("qD4znHH4EBDkzbEMSj-YajB53Izi734YIjx5CVEiKgU");
        templateMessage.setToUser(openid);
        SimpleDateFormat sdf =   new SimpleDateFormat( " yyyy-MM-dd HH:mm:ss " );
        List<WxMpTemplateData> data = Arrays.asList(
                new WxMpTemplateData("first","在此通知您，管理员已对您的文章已审阅"),
                new WxMpTemplateData("keyword1",result),
                new WxMpTemplateData("keyword2",title),
                new WxMpTemplateData("keyword3",sdf.format(new Date())),
                new WxMpTemplateData("remark","有何不服请联系管理员")
        );
        templateMessage.setData(data);
        templateMessage.setUrl("http://xinlovezhang.top:8080/ftl/auth");   //设置链接，不知可行不
        try {
            wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);

        }catch(WxErrorException e){
            log.error("【微信模板消息】发送失败，{}",e);
        }
    }

    public void successSign(String openid) {
        WxMpTemplateMessage templateMessage = new WxMpTemplateMessage();
        templateMessage.setTemplateId("xczEOhkQ7j4W2tvWwomvR3wzGRCyjlUyPPaaapoEdGM");
        templateMessage.setToUser(openid);
        SimpleDateFormat sdf =   new SimpleDateFormat( " yyyy-MM-dd HH:mm:ss " );
        List<WxMpTemplateData> data = Arrays.asList(
                new WxMpTemplateData("first","你好，欢迎通过微信授权登陆楠尼玛summerNote编辑器"),
                new WxMpTemplateData("keyword1","楠尼玛summerNote编辑器"),
                new WxMpTemplateData("keyword2","http://yaohaonan.natapp1.cc/demo/index"),
                new WxMpTemplateData("keyword4",sdf.format(new Date())),
                new WxMpTemplateData("remark","如有疑问请邮箱519286925@qq.com")
        );
        templateMessage.setData(data);
        try {
            wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);

        }catch(WxErrorException e){
            log.error("【微信模板消息】发送失败，{}",e);
        }
    }


}
