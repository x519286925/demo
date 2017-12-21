package com.test.demo.service;

import com.test.demo.entity.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * Created by yhn on 2017/9/7.
 */
public interface UserService {
    User findOne(String openid);
    User save(User userInfo);
    String qcodeImgUrl(String codeContent, HttpServletRequest request);
    User findById(String id);
    List<User> findAll();
}
