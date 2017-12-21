package com.test.demo.enums;

import lombok.Getter;

/**
 * Created by yhn on 2017/11/14.
 */
@Getter
public enum NewStatus implements CodeEnum {
    ALLOW(0,"审核通过"),
    WANT_ALLOW(1,"未审核"),
    NOT_ALLOW(2,"审核不通过")
    ;
    private Integer code;
    private String message;
    NewStatus(Integer code, String message) {
        this.code = code;
        this.message=message;
    }
}
