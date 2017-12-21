package com.test.demo.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by yhn on 2017/10/31.
 */
public interface UploadFileService {
    /**
     * 第一个参数:文件源
     * 第二个参数：请求
     * 第三个参数：允许文件类型
     * 第四个参数：文件夹名称
     * 第五个参数：域名->方便直接显示
     * description:上传失败直接返回：uploadFail
     */
     String uploadFile(MultipartFile file,HttpServletRequest request,String allowType, String folderName, String fileName,String ImgUrl);
}
