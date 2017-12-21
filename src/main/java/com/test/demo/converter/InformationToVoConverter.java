package com.test.demo.converter;

import com.test.demo.VO.InformationListVO;
import com.test.demo.entity.monEntity.Information;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by yhn on 2017/11/4.
 */
public class InformationToVoConverter {
    public static InformationListVO convert(Information information){
        InformationListVO informationListVO = new InformationListVO();
        BeanUtils.copyProperties(information,informationListVO);
        return informationListVO;
    }
    public static List<InformationListVO> convert(List<Information> information){
        return information.stream().map(e ->convert(e)).collect(Collectors.toList());
    }
}
