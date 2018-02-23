package com.apass.zufang.service.nation;

import com.apass.esp.common.code.BusinessErrorCode;
import com.apass.gfb.framework.exception.BusinessException;
import com.apass.zufang.domain.common.WorkCityJd;
import com.apass.zufang.mapper.zfang.WorkCityJdMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

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
	private static final Logger LOGGER = LoggerFactory.getLogger(NationService.class);
    /**
     * 直辖市
     */
    private static final String[] CENTRL_CITY_ARRAY = {"110000", "120000", "310000", "500000"};
    private static final List<String> CENTRL_CITY_LIST = Arrays.asList(CENTRL_CITY_ARRAY);

    /**
     * 新疆、西藏、甘肃省、宁夏、内蒙古、青海省、香港、澳门、台湾省
     */
    private static final List<String> FORBIDDEN_PROVINCE = Arrays.asList(
            new String[]{"650000", "540000", "620000", "640000", "150000", "630000", "810000", "820000", "710000"});
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
    

}
