package com.test.demo.service;

import com.test.demo.entity.monEntity.Information;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by yhn on 2017/11/1.
 */
public interface MonNewsService {
    Information save(Information monNews);
    Information findById(String id);
    List<Information> findByUserId(String userId);
    Page<Information> findByDisplay(Integer display,Pageable pageable);
}
