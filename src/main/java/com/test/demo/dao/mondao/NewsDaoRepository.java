package com.test.demo.dao.mondao;

import com.test.demo.entity.monEntity.Information;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by yhn on 2017/11/1.
 */
public interface NewsDaoRepository extends MongoRepository<Information,String>{
    Information findById(String id);  //根据id查找
    List<Information> findByUserId(String userid);  //未分页，根据用户id查找
    Page<Information> findByDisplay(Integer display,Pageable pageable);
}
