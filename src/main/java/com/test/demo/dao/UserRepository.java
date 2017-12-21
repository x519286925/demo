package com.test.demo.dao;

import com.test.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by yhn on 2017/9/7.
 */
public interface UserRepository extends JpaRepository<User,String>{
    User findByOpenId(String openid);  //根据openid 查询用户信息
    User findById(String id);
}
