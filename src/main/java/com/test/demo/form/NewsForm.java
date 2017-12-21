package com.test.demo.form;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by yhn on 2017/11/1.
 */
@Data
public class NewsForm {   //发表文章的表单
    @NotEmpty
    private String title;  //文章标题
    @NotEmpty
    private String content; //文章内容
    @NotEmpty(message = "缩略图不能为空")
    private String photo; //缩略图
    @NotEmpty(message = "描述不能为空")
    private String description; //描述
    @NotEmpty(message = "标签不能为空")
    private String type;  //类型---->  标签

}
