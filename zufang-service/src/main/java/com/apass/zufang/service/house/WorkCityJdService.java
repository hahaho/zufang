package com.apass.zufang.service.house;

import com.apass.gfb.framework.exception.BusinessException;
import com.apass.zufang.domain.common.WorkCityJd;
import com.apass.zufang.mapper.zfang.WorkCityJdMapper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by DELL on 2018/4/24.
 */
@Service
public class WorkCityJdService {

    @Autowired
    private WorkCityJdMapper workCityJdMapper;

    public WorkCityJd getByCityName(String city) throws BusinessException{
       WorkCityJd workCityJd = workCityJdMapper.selectByCityName(city);
       if(workCityJd == null){
           throw new BusinessException("未查询到该省份，city = " + city);
       }
       return workCityJdMapper.selectWorkCityByCode(workCityJd.getParent().toString());
    }

}
