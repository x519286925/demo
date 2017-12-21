package com.test.demo.controller;

import com.test.demo.config.UrlConfig;
import com.test.demo.entity.Manager;
import com.test.demo.entity.User;
import com.test.demo.enums.PowerEnum;
import com.test.demo.service.ManagerService;
import com.test.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

/**
 * Created by yhn on 2017/11/15.
 */
@RestController
@Slf4j
public class SuperManagerController {
    @Autowired
    private ManagerService managerService;
    @Autowired
    private UserService userService;
    @Autowired
    private UrlConfig urlConfig;
    @GetMapping("/superManagerIndex")
    public ModelAndView superManagerIndex(@RequestParam("openid") String openid,
                                          @RequestParam("headImage") String headImage,
                                          Map<String,Object> map){
        List<User> userList =  userService.findAll();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String managerOpenid = (String)attributes.getRequest().getSession().getAttribute("userid");
        String power =  managerService.findByOpenid(managerOpenid).getPower();
        map.put("userList",userList);
        map.put("projectUrl",urlConfig.getUrl()+urlConfig.getContextPath());
        map.put("power",power);
        return new ModelAndView("page/manager/superManager");
        }
        @GetMapping("/getRoleName")    //获得角色
        public String getRoleName(@RequestParam("openid") String openid){
             Manager manager =  managerService.findByOpenid(openid);
             if(manager.getPower().equals(String.valueOf(PowerEnum.SUPER_ADMIN.getCode()))){
                return PowerEnum.SUPER_ADMIN.getMessage();
            }else if(manager.getPower().equals(String.valueOf(PowerEnum.ADMIN.getCode()))){
                 return PowerEnum.ADMIN.getMessage();
             }
             else{
                 return PowerEnum.USER.getMessage();
             }
        }
        @GetMapping("/managerforPower")
        public String managerforPower(@RequestParam("openid") String openid,
                                      @RequestParam("power") String power ) {
            if(power.equals("1")){
                return "操作失败，休想再弄一个超级管理员";
            }
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            String managerOpenid = (String)attributes.getRequest().getSession().getAttribute("userid");
            Manager manager =  managerService.findByOpenid(managerOpenid);   //得到操作人的信息
            Manager user = managerService.findByOpenid(openid);  //被操作人
            if(manager.getPower().equals(String.valueOf(PowerEnum.SUPER_ADMIN.getCode()))){   //如果是超级管理员
                    if(user.getPower().equals(String.valueOf(PowerEnum.SUPER_ADMIN.getCode()))){   //如果操作对象是超级管理员
                            return "操作失败，无法对超级管理员进行任何操作";
                    }
                    else if(user.getPower().equals(String.valueOf(PowerEnum.ADMIN.getCode()))){   //如果操作对象是管理员
                           if(user.getPower().equals(power)){
                               return "操作失败，无法重复操作";
                           }
                            user.setPower(power);
                            managerService.save(manager);
                            return "操作成功，已更改对方身份";
                    }
                    else{     //操作对象是读者
                        if(user.getPower().equals(power)){
                            return "操作失败，无法重复操作";
                        }
                        user.setPower(power);
                        managerService.save(manager);
                        return "操作成功，已更改对方身份";
                    }
            }else if(manager.getPower().equals(String.valueOf(PowerEnum.ADMIN.getCode()))){    //如果是管理员
                    return "操作失败，管理员只可对用户进行拉黑操作";
            }
            else{
                return "操作失败，无权限";
            }
        }
    }
