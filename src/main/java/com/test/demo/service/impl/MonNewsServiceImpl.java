package com.test.demo.service.impl;

import com.test.demo.dao.mondao.NewsDaoRepository;
import com.test.demo.entity.monEntity.Information;
import com.test.demo.service.MonNewsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yhn on 2017/11/1.
 */
@Service
@Slf4j
public class MonNewsServiceImpl implements MonNewsService{
    @Autowired
    private NewsDaoRepository newsDaoRepository;
    @Override
    public Information save(Information monNews) {
        return newsDaoRepository.save(monNews);
    }

    @Override
    public Information findById(String id) {
        return newsDaoRepository.findById(id);
    }

    @Override
    public List<Information> findByUserId(String userId) {
        return newsDaoRepository.findByUserId(userId);
    }

    @Override
    public Page<Information> findByDisplay(Integer display,Pageable pageable) {
        Sort sort = new Sort(Sort.Direction.ASC,"createTime");   //---根据创建时间来进行升序
        log.info("page={},size={}",pageable.getPageNumber(),pageable.getPageSize());
        PageRequest request = new PageRequest(pageable.getPageNumber(),pageable.getPageSize(),sort);
        Page<Information> informationPage = newsDaoRepository.findByDisplay(display,request);
        return informationPage;
    }
}
