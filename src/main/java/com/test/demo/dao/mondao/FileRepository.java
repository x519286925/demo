package com.test.demo.dao.mondao;

import com.test.demo.entity.monEntity.File;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by yhn on 2017/11/8.
 */
public interface FileRepository extends MongoRepository<File,String>{

}
