package com.apass.zufang.mapper.zfang;


import com.apass.gfb.framework.mybatis.GenericMapper;
import com.apass.zufang.domain.common.WorkCityJd;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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
	

	
}
