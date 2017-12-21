package com.test.demo.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;

import static org.junit.Assert.*;

/**
 * Created by yhn on 2017/11/6.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MongoTemplateServiceImplTest {
    @Autowired
    private MongoTemplateServiceImpl mongoTemplateService;
    @Test
    public void saveFile() throws Exception {
       File file = new File("D:/IMG_20160415_185018.jpg");
       mongoTemplateService.SaveFile("Collections",file,"fileid","companyid","nan");
    }

    @Test
    public void retrieveFileOne() throws Exception {

    }

}