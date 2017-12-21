package com.test.demo.service.impl;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.test.demo.config.UrlConfig;
import com.test.demo.dao.UserRepository;
import com.test.demo.entity.User;
import com.test.demo.service.UserService;
import com.test.demo.utils.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;

/**
 * Created by yhn on 2017/9/7.
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UrlConfig urlConfig;
    @Override
    public User findOne(String openid) {
        return userRepository.findByOpenId(openid);
    }

    @Override
    public User save(User userInfo) {
        return  userRepository.save(userInfo);
    }

    @Override
    public String qcodeImgUrl(String codeContent, HttpServletRequest request) {
        int width = 350;   //图片的大小
        int height = 350;
        String format = "jpg";
        String fileName = KeyUtil.genUniqueKey();  //文件名
        String path  = request.getSession().getServletContext().getRealPath("/");
        System.out.println("路径："+path);
        //创建二维码内容
        String content = codeContent;
        //定义二维码参数
        HashMap hints = new HashMap();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");  //定义内容的编码
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);   //误差校正:一般M
        hints.put(EncodeHintType.MARGIN, 2);//设置边距:如边框空白
        try {
            BitMatrix bitMatrix  = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height,hints);
            path = path+ fileName+".jpg";
            Path file = new File(path).toPath();
            MatrixToImageWriter.writeToPath(bitMatrix, format, file);
        } catch (WriterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return urlConfig.getUrl()+urlConfig.getContextPath()+"/"+fileName+".jpg";
    }

    @Override
    public List<User> findAll() {
       return userRepository.findAll();
    }

    @Override
    public User findById(String id) {
        return userRepository.findById(id);
    }
}
