package com.test.demo.service.impl;

import com.test.demo.dao.ManagerRepository;
import com.test.demo.entity.Manager;
import com.test.demo.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by yhn on 2017/11/14.
 */
@Service
public class ManagerServiceImpl implements ManagerService{
    @Autowired
    private ManagerRepository managerRepository;
    @Override
    public Manager findByOpenid(String openid) {
        return managerRepository.findByOpenid(openid);
    }

    @Override
    public Manager deleteByOpenid(String openid) {
        return managerRepository.deleteByOpenid(openid);
    }

    @Override
    public Manager save(Manager manager) {
        return managerRepository.save(manager);
    }
}
