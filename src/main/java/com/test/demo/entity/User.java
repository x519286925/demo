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
public class User {
    @Id
    private String id; //id

    private String nickName; //昵称 一开始默认随机数

    private String headImage; //头像

    private String description;  //描述

    private String openId;  //微信openId

    private String phone;  //手机号

    private String region;  //地区

    private Integer status;  //状态 默认0为正常状态，1为拉黑状态

    private Integer publisher;//是否为可发布者 默认0 不是发布者

    private Date createTime;  //创建时间

    private Date updateTime;  //更新时间
}