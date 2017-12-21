package com.test.demo.dao;

import com.test.demo.entity.ScanLogin;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by yhn on 2017/11/3.
 */
public interface ScanLoginRepository extends JpaRepository<ScanLogin,String> {
    ScanLogin findById(String id);
}
