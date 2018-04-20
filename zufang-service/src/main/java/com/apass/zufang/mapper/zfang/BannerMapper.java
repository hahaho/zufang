package com.apass.zufang.mapper.zfang;

import java.util.List;

import com.apass.gfb.framework.mybatis.GenericMapper;
import com.apass.zufang.domain.entity.Banner;

public interface BannerMapper extends GenericMapper<Banner,Long> {

	List<Banner> loadIndexBanners(String type);
	
	 List<Banner> getActivityUrlLikeActivityId(String activityId);
}
