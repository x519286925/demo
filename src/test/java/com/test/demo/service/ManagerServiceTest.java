package com.test.demo.service;

import com.test.demo.entity.Manager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * Created by yhn on 2017/11/14.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ManagerServiceTest {
    @Autowired
    private ManagerService managerService;
    @Test
    public void findByOpenid() throws Exception {

    }

    @Test
    public void deleteByOpenid() throws Exception {
    }

    @Test
    public void save() throws Exception {
        Manager manager = new Manager();
        manager.setId("123");
        manager.setOpenid("456");
        manager.setPower("admin");
        managerService.save(manager);
    }


}