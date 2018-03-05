package com.apass.zufang.mapper.zfang;

import java.util.HashMap;
import java.util.List;

import com.apass.gfb.framework.mybatis.GenericMapper;
import com.apass.zufang.domain.entity.HouseLocation;
import com.apass.zufang.domain.vo.HouseVo;

/**
 * Created by DELL on 2018/2/8.
 */
public interface HouseLocationMapper extends GenericMapper<HouseLocation,Long>{
	
	HouseLocation getLocationByHouseId(Long houseId);
	
	List<HouseVo> initHouseByCity(HashMap<String, String> map);
	
}
