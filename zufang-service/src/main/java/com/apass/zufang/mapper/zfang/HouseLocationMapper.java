package com.apass.zufang.mapper.zfang;

import java.util.List;
import java.util.Map;

import com.apass.gfb.framework.mybatis.GenericMapper;
import com.apass.zufang.domain.entity.HouseLocation;
import com.apass.zufang.domain.vo.HouseVo;

/**
 * Created by DELL on 2018/2/8.
 */
public interface HouseLocationMapper extends GenericMapper<HouseLocation,Long>{
	
	HouseLocation getLocationByHouseId(Long houseId);
	
	List<HouseVo> initPeizhiLocation(HouseLocation houseLocation);

	List<HouseVo> initHotLocation(HouseLocation houseLocation);

	List<HouseVo> initNearLocation(Map<String, Double> returnLLSquarePoint);
	
	
}
