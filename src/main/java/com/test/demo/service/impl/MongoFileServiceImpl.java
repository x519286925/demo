package com.test.demo.service.impl;

import com.test.demo.dao.mondao.FileRepository;
import com.test.demo.entity.monEntity.File;
import com.test.demo.service.MongoFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

/**
 * Created by yhn on 2017/11/8.
 */
@Service
public class MongoFileServiceImpl implements MongoFileService{
   @Autowired
   private FileRepository fileRepository;
    @Override
    public File save(File file) {
        return fileRepository.save(file);
    }

    @Override
    public File getFileById(String id) {
        return fileRepository.findOne(id);
    }
}
