package com.apass.zufang.mapper.zfang;

import com.apass.gfb.framework.mybatis.GenericMapper;
import com.apass.gfb.framework.mybatis.page.Pagination;
import com.apass.zufang.domain.entity.AppVersionEntity;

public interface AppVersionMapper extends GenericMapper<AppVersionEntity, Long> {

	Pagination<AppVersionEntity> getVersionPage(AppVersionEntity version);

	AppVersionEntity select(Long valueOf);

}
