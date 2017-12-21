package com.test.demo.dao;

import com.test.demo.entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by yhn on 2017/11/14.
 */
public interface ManagerRepository extends JpaRepository<Manager,String> {
    Manager findByOpenid(String openid);
    Manager deleteByOpenid(String openid);
}
