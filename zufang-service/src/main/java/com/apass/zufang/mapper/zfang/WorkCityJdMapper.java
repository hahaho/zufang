package com.apass.zufang.mapper.zfang;


import com.apass.gfb.framework.mybatis.GenericMapper;
import com.apass.zufang.domain.common.WorkCityJd;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WorkCityJdMapper extends GenericMapper<WorkCityJd, Long> {
	/**
	 * 查询所有的城市id
	 * @return
	 */
	List<String> selectAllCity();
	List<String> selectDistrict();
	Integer selectByCode(String code);

	/**
	 * 根据parent查询子集（省、市或区）
	 * @param code
	 * @return
     */
	List<WorkCityJd> selectDateByParentId(@Param("code") String code);
	
}
