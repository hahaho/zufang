package com.apass.zufang.mapper.zfang;

import java.util.List;

import com.apass.gfb.framework.mybatis.GenericMapper;
import com.apass.zufang.domain.dto.BannerQueryParams;
import com.apass.zufang.domain.entity.Banner;
import com.apass.zufang.domain.vo.BannerVo;

public interface BannerMapper extends GenericMapper<Banner,Long> {

	/*** 根据banner 类型查询Banner*/
	List<Banner> loadIndexBanners(BannerQueryParams entity);
	 
	 /*** 房源信息管理* @param entity  @return*/
    List<BannerVo> getBannerLists(BannerQueryParams entity);
	
	/** 房源信息管理数量查询 @param entity  @return*/
	Integer getBannerListsCount(BannerQueryParams entity);
}
