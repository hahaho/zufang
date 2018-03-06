package com.apass.zufang.mapper.zfang;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.apass.gfb.framework.mybatis.GenericMapper;
import com.apass.zufang.domain.common.WorkCityJd;
import com.apass.zufang.domain.dto.WorkCityJdParams;

/**
 * 地址相关公共方法
 * @author xiaohai
 */
public interface WorkCityJdMapper extends GenericMapper<WorkCityJd, Long> {
	/**
	 * 根据parent查询子集（省、市或区）
	 * @param code
	 * @return
     */
	List<WorkCityJd> selectDateByParentId(@Param("code") String code);
	/**
	 * 根据传入的province city district towns 获取对应的Code
	 * @return
	 */
	WorkCityJd selectCodeByName(WorkCityJdParams entity);

	WorkCityJd selectWorkCityByCode(String areaCode);
}
