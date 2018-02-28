package com.apass.zufang.mapper.zfang;

import java.util.List;
import java.util.Map;

import com.apass.gfb.framework.mybatis.GenericMapper;
import com.apass.zufang.domain.entity.HouseInfoRela;

/**
 * Created by DELL on 2018/2/8.
 */
public interface HouseInfoRelaMapper extends GenericMapper<HouseInfoRela, Long> {
	/**
	 * 查询房源相关信息
	 * 
	 * @param entity
	 * @return
	 */
	List<HouseInfoRela> getHouseInfoRelaList(HouseInfoRela entity);

	/**
	 * 根据houseId查询房源信息
	 * 
	 * @param paraMap
	 * @return
	 */
	HouseInfoRela getHouseInfoByHouseId(Long houseId);
	

}
