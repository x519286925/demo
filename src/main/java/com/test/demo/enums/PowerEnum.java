package com.test.demo.enums;

import lombok.Getter;

/**
 * Created by yhn on 2017/7/31.
 */
@Getter
public enum PowerEnum implements CodeEnum{
    SUPER_ADMIN(1,"超级管理员"),
    ADMIN(2,"管理员"),
    USER(0,"用户读者"),
    ;
    private Integer code;
    private String message;
    PowerEnum(Integer code, String message) {
        this.code = code;
        this.message=message;
    }
}
