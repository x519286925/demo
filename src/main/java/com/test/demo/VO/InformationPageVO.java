package com.test.demo.VO;

import com.test.demo.entity.monEntity.Information;
import lombok.Data;

import java.util.List;

/**
 * Created by yhn on 2017/11/14.
 */
@Data
public class InformationPageVO {
        private List<Information> informationList;
        private String page;
        private String size;
        private String pageContent;  //是否还有内容

}
