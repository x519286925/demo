package com.test.demo.service;

/**
 * Created by yhn on 2017/9/6.
 */
public interface PushMessageService {
    //签到成功
    void successSign(String openid);
    //审核结果
    void newsResult(String openid,String result,String title);
}
