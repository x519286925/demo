package com.test.demo.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by yhn on 2017/11/3.
 */
@Entity
@Data
@DynamicUpdate
public class ScanLogin {
    @Id
    private String id; //id     //websocketId
    private Integer status;   //状态，0为未被扫过, 1为被扫过。
    private String openid;    //用户的openid
    private Integer loginStatus;   //是否成功登陆    0为未成功登陆,1为成功登陆过
    private String qcodeImgUrl;   //二维码路径
    private Date createTime;  //创建时间
}
