package com.test.demo.service;

import com.test.demo.entity.ScanLogin;

/**
 * Created by yhn on 2017/11/3.
 */
public interface ScanLoginService {
    ScanLogin save(ScanLogin scanLogin);
    ScanLogin findById(String id);
}
