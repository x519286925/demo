package com.test.demo.service.impl;

import com.test.demo.config.UrlConfig;
import com.test.demo.service.UploadFileService;
import com.test.demo.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by yhn on 2017/10/31.
 */
@Service
@Slf4j
@Scope("prototype")
public class UploadFileServiceImpl implements UploadFileService{
    @Autowired
    private UrlConfig urlConfig;
    @Override
    public String uploadFile(MultipartFile file,
                             HttpServletRequest request,
                             String allowType,
                             String folderName,
                             String fileName,
                             String ImgUrl) {
        String contentType = file.getContentType();
        log.info("【文件类型为】，contentType={}",contentType);
        if(contentType.indexOf(allowType)==-1){   //不是图片
            log.error("【上传图片类型不正确】,contentType={}",contentType);
            return "uploadFail";
        }
//        String fileName = file.getOriginalFilename();   //上传的文件名，一般不采用
        String filePath = request.getSession().getServletContext().getRealPath(folderName+"/");
        System.out.println("路径---->"+filePath);
        try {
            FileUtil.uploadFile(file.getBytes(), filePath, fileName);
        } catch (Exception e) {
            e.printStackTrace();
            return "uploadFail";
        }
        return ImgUrl+urlConfig.getContextPath()+"/"+folderName+"/"+fileName;
    }
}
