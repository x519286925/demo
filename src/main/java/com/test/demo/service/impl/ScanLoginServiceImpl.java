package com.test.demo.service.impl;

import com.test.demo.dao.ScanLoginRepository;
import com.test.demo.entity.ScanLogin;
import com.test.demo.service.ScanLoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by yhn on 2017/11/3.
 */
@Service
@Slf4j
public class ScanLoginServiceImpl implements ScanLoginService{
    @Autowired
    private ScanLoginRepository scanLoginRepository;
    @Override
    public ScanLogin save(ScanLogin scanLogin) {
        return scanLoginRepository.save(scanLogin);
    }

    @Override
    public ScanLogin findById(String id) {
        return scanLoginRepository.findById(id);
    }
}
