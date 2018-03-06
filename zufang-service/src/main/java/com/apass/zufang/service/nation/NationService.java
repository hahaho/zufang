package com.apass.zufang.service.nation;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apass.gfb.framework.exception.BusinessException;
import com.apass.zufang.domain.common.WorkCityJd;
import com.apass.zufang.mapper.zfang.WorkCityJdMapper;

/**
 * 查询省份市
 *
 * @author zengqingshan
 * @version $Id: NationService.java, v 0.1 2016年12月21日 下午3:57:07 zengqingshan
 *          Exp $
 * @description
 */
@Service
public class NationService {
	
    @Autowired
    private WorkCityJdMapper cityJdMapper;

    /**
     * 根据code,查询数据
     *
     * @return
     * @throws BusinessException
     */
    public List<WorkCityJd> queryDistrictForAms(String districtCode){
    	return cityJdMapper.selectDateByParentId(districtCode);
    }


    public WorkCityJd selectWorkCityByCode(String areaCode) {
        return cityJdMapper.selectWorkCityByCode(areaCode);
    }
}
