package com.test.demo.service;

import com.test.demo.entity.Manager;

/**
 * Created by yhn on 2017/11/14.
 */
public interface ManagerService {
    Manager findByOpenid(String openid);  //通过openid查询
    Manager deleteByOpenid(String openid);  //通过openid删除
    Manager save(Manager manager);   //保存
}
