package com.test.demo.service;

import com.test.demo.entity.monEntity.File;

/**
 * Created by yhn on 2017/11/8.
 */
public interface MongoFileService {
    File save(File file);
    File getFileById(String id);
}
