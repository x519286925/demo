package com.test.demo.entity.monEntity;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by yhn on 2017/11/1.
 */
@Data
public class Information{
    @Id
    private String id;   //文章或新闻 id
    private String title;  //文章标题
    private String content; //文章内容
    private String photo; //缩略图
    private String description; //描述
    private String type;  //类型---->  标签
    private Integer readingQuantity; //阅读量
    private String userId;  //用户id--->所属用户
    private Integer display;  //是否显示  0为显示 1为禁止显示----审核
    private String displayForPhone;  //手机预览
    private String  createTime;
    private String updateTime;
}
