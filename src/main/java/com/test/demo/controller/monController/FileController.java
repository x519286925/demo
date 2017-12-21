package com.test.demo.controller.monController;

import com.test.demo.config.UrlConfig;
import com.test.demo.entity.monEntity.File;
import com.test.demo.service.MongoFileService;
import com.test.demo.utils.KeyUtil;
import com.test.demo.utils.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by yhn on 2017/11/8.
 */
@RestController
@Slf4j
public class FileController {
    @Autowired
    private MongoFileService mongoFileService;
    @Autowired
    private UrlConfig urlConfig;
    @PostMapping("/testuploadimg2")
    public String success(@RequestParam("file") MultipartFile file,
                          HttpServletRequest request){
        String fileId = "";
        try {
            File f = new File(file.getOriginalFilename(), file.getContentType(), file.getSize(), file.getBytes());
            f.setMd5(MD5Util.getMD5(file.getInputStream()) );
            fileId = KeyUtil.genUniqueKey();
            f.setId(fileId);
            mongoFileService.save(f);
            log.info("mongoDB存图片成功！fileId={}",fileId);
        } catch (IOException | NoSuchAlgorithmException ex) {
            ex.printStackTrace();
            log.error("【MongoDB上传文件错误】:fileId={}",fileId);
            return null;
        }
        return urlConfig.getUrl()+urlConfig.getContextPath()+"/file/"+fileId;
    }
    @GetMapping("/test")
    public ModelAndView test(){
        return new ModelAndView("page/upload");
    }
    @GetMapping("/file/{id}")
    public byte[] serveFile(@PathVariable String id) {    //回显图片
        File file = mongoFileService.getFileById(id);
        if(file!=null){
            return file.getContent();
        }
        else{
            log.error("mongoDB查询文件不存在！ id={}",id);
            return null;
        }
    }
}
