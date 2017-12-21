package com.test.demo.handle;

import com.test.demo.config.UrlConfig;
import com.test.demo.exception.ManagerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yhn on 2017/9/6.
 */
@ControllerAdvice
public class SellerHandleException {
    @Autowired
    private UrlConfig urlConfig;
    //  拦截登陆异常
    @ExceptionHandler(value = ManagerException.class)
    public ModelAndView handlerAuthorizeException(){
        Map<String,Object> map =  new HashMap<>();
        map.put("msg","您无权限进入管理员页面");
        return new ModelAndView("common/error",map);
    }
}
