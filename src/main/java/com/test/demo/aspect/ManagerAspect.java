package com.test.demo.aspect;

import com.test.demo.entity.Manager;
import com.test.demo.entity.User;
import com.test.demo.enums.PowerEnum;
import com.test.demo.exception.ManagerException;
import com.test.demo.service.ManagerService;
import com.test.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;
/**
 * Created by yhn on 2017/11/14.
 */
@Aspect
@Component
@Slf4j
public class ManagerAspect {
    @Autowired
    private UserService userService;
    @Autowired
    private ManagerService managerService;
    @Pointcut("(execution(public * com.test.demo.controller.ManagerController.*(..)))"+"||"+"(execution(public * com.test.demo.controller.SuperManagerController.*(..)))")
    public void verify(){} //验证
    @Before("verify()")
    public void doerify() {    //方法的具体实现
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String sessionOpenid = (String)attributes.getRequest().getSession().getAttribute("userid");  //openid
        log.info("session={}",sessionOpenid);
        User user = userService.findOne(sessionOpenid);
        Map<String,Object> map = new HashMap<>();
        if(user==null){
            log.error("because:user not exist");
            throw new ManagerException();
        }
        Manager manager =  managerService.findByOpenid(sessionOpenid);
        if(manager==null){
            log.error("because：not manger infomation");
            throw new ManagerException();
        }
        String code = manager.getPower();
        if(code.equals(String.valueOf(PowerEnum.USER.getCode()))){   //如果是读者  您无权限进入管理员页面
            throw new ManagerException();
        }
        String power = "undefind";
        if(code.equals(String.valueOf(PowerEnum.SUPER_ADMIN.getCode()))){
            power = String.valueOf(PowerEnum.SUPER_ADMIN.getMessage());
        }
        if(code.equals(String.valueOf(PowerEnum.ADMIN.getCode()))){
            power = String.valueOf(PowerEnum.ADMIN.getMessage());
        }
        if(power.equals("undefind")){
            map.put("msg","您无权限进入管理员页面");
            log.error("未进入管理员页面原因：无此权限");
            throw new ManagerException();
        }
    }
}
