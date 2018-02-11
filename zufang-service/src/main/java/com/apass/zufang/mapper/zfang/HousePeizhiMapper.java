package com.apass.zufang.mapper.zfang;

import java.util.List;

import com.apass.gfb.framework.mybatis.GenericMapper;
import com.apass.zufang.domain.entity.HousePeizhi;

/**
 * Created by DELL on 2018/2/7.
 */
public interface HousePeizhiMapper extends GenericMapper<HousePeizhi,Long>{
	
	List<HousePeizhi> getPeiZhiByHouseId(Long houseId);
}
