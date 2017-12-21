package com.test.demo.controller;

import com.test.demo.VO.InformationPageVO;
import com.test.demo.config.UrlConfig;
import com.test.demo.dao.mondao.NewsDaoRepository;
import com.test.demo.entity.Manager;
import com.test.demo.entity.User;
import com.test.demo.entity.monEntity.Information;
import com.test.demo.enums.NewStatus;
import com.test.demo.enums.PowerEnum;
import com.test.demo.service.ManagerService;
import com.test.demo.service.MonNewsService;
import com.test.demo.service.PushMessageService;
import com.test.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

/**
 * Created by yhn on 2017/11/13.
 */
@RestController
@Slf4j
public class ManagerController {
    @Autowired
    private UserService userService;
    @Autowired
    private ManagerService managerService;
    @Autowired
    private MonNewsService monNewsService;
    @Autowired
    private UrlConfig urlConfig;
    @Autowired
    PushMessageService pushMessageService;
    @GetMapping("/managerIndex")     //先初始化分页,也可以直接跳转页面，在这里方便调试，我将先初始化分页。
    public ModelAndView modelAndView(@RequestParam("openid") String openid,
                                     @RequestParam("headImage") String headImage,
                                     @RequestParam(value = "page",defaultValue ="1" ) Integer page,
                                     @RequestParam(value = "size",defaultValue ="7") Integer size,
                                     Map<String,Object> map){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String sessionOpenid = (String)attributes.getRequest().getSession().getAttribute("userid");  //把openid存进session
        User user = userService.findOne(sessionOpenid);
        Manager manager =  managerService.findByOpenid(openid);
        String code = manager.getPower();
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
            return new ModelAndView("common/error");
        }
        map.put("power",power);    //用户名称
        PageRequest request = new PageRequest(page-1,size);
        Page<Information> informationList = monNewsService.findByDisplay(1,request);
        if(page<informationList.getTotalPages()){   //是否还有内容
            map.put("pageContent","true");
        }
        else{
            map.put("pageContent","false");
        }
        map.put("size",size);
        map.put("page",page);
        map.put("informationList",informationList.getContent());
        map.put("projectUrl",urlConfig.getUrl()+urlConfig.getContextPath());
        return new ModelAndView("page/manager/index",map);
    }
    @GetMapping("/managerIndexPage")   //分页查找需要的分页信息
    public InformationPageVO modelAndView(@RequestParam(value = "page",defaultValue ="1" ) String page,
                                          @RequestParam(value = "size",defaultValue ="7") String size
                                         ) {
        InformationPageVO informationPageVO = new InformationPageVO();
        PageRequest request = new PageRequest(Integer.valueOf(page)-1,Integer.valueOf(size));
        Page<Information> informationList = monNewsService.findByDisplay(1,request);
        if(Integer.valueOf(page)<informationList.getTotalPages()){   //是否还有内容
            informationPageVO.setPageContent("true");
        }
        else{
            informationPageVO.setPageContent("false");
        }
        informationPageVO.setPage(page);
        informationPageVO.setSize(size);
        informationPageVO.setInformationList(informationList.getContent());
        log.info("informationPageVO={}",informationPageVO);
        return informationPageVO;
    }


    @GetMapping("/managerCheckNews")
    public ModelAndView managerCheckNews(@RequestParam("id") String id, Map<String,Object> map){
          Information information =   monNewsService.findById(id);
          if(information==null){
              map.put("msg","该文章不存在");
              return new ModelAndView("common/error");
          }
        map.put("information",information);
        map.put("projectUrl",urlConfig.getUrl()+urlConfig.getContextPath());
          return new ModelAndView("page/manager/newsDetail");
    }
    @GetMapping("/managerforNews")
    public String managerforNews(@RequestParam("id") String id,
                        @RequestParam("action") String action, Map<String,Object> map) {
        Information information = monNewsService.findById(id);
        User user = userService.findById(information.getUserId());   //得到用户
        if (information == null) {
            return "fail";
        }
        if (action.equals(String.valueOf(NewStatus.ALLOW.getCode()))) {
            if(information.getDisplay().equals(NewStatus.ALLOW.getCode())){
                return "fail";
            }
            information.setDisplay(NewStatus.ALLOW.getCode());  //审阅通过
            monNewsService.save(information);
            pushMessageService.newsResult(user.getOpenId(), "恭喜您审核通过", information.getTitle());
            return "success";
        }
        if (action.equals(String.valueOf(NewStatus.NOT_ALLOW.getCode()))) {
            if(information.getDisplay().equals(NewStatus.NOT_ALLOW.getCode())){
                return "fail";
            }
            information.setDisplay(NewStatus.NOT_ALLOW.getCode());  //审阅不通过
            monNewsService.save(information);
            pushMessageService.newsResult(user.getOpenId(), "抱歉审核未通过", information.getTitle());
            return "not_success";
        }
        return "fail";
    }
}
