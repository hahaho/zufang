package com.apass.zufang.mapper.zfang;

import java.util.List;

import com.apass.gfb.framework.mybatis.GenericMapper;
import com.apass.zufang.domain.dto.AppVersionQueryParams;
import com.apass.zufang.domain.entity.AppVersionEntity;

public interface AppVersionMapper extends GenericMapper<AppVersionEntity, Long> {
 
	public List<AppVersionEntity> getVersionPage(AppVersionQueryParams entity);
	 
	public Integer getVersionPageCount(AppVersionQueryParams entity);
	

	AppVersionEntity select(Long valueOf);

}
