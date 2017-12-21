package com.test.demo.VO;

import lombok.Data;

/**
 * Created by yhn on 2017/11/3.
 */
@Data
public class ScanDataVO {
    private String LoginStatus;   //成功:success,失败fail
    private String openid;        //用户的微信openid
}
