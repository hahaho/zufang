package com.apass.zufang.service.version;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apass.gfb.framework.cache.CacheManager;
import com.apass.gfb.framework.exception.BusinessException;
import com.apass.gfb.framework.mybatis.page.Pagination;
import com.apass.gfb.framework.utils.GsonUtils;
import com.apass.zufang.domain.entity.AppVersionEntity;
import com.apass.zufang.mapper.zfang.AppVersionMapper;

@Service
public class AppVersionService {

    private static final String APPVERSION = "APP_VERSION_CODE";

    @Autowired
    private AppVersionMapper appVersionDao;
    @Autowired
    private CacheManager cacheManage;

    public Pagination<AppVersionEntity> getVersionPage(AppVersionEntity version) {
        Pagination<AppVersionEntity> pagination = appVersionDao.getVersionPage(version);
        String isReleasedJson = cacheManage.get(APPVERSION);
        if(!StringUtils.isEmpty(isReleasedJson)){
            AppVersionEntity releasedVer = GsonUtils.convertObj(isReleasedJson, AppVersionEntity.class);
            List<AppVersionEntity> versionList = pagination.getDataList();
            for (AppVersionEntity ave : versionList) {
                if (ave.getId()==releasedVer.getId()) {
                    ave.setReleased(true);
                }
            }
        }
        return pagination;
    }


    public void save(AppVersionEntity appVersion) {
        appVersionDao.insert(appVersion);
    }

    public void deployVersion(AppVersionEntity version) throws BusinessException {
    	
        Pagination<AppVersionEntity> appVersion;
		AppVersionEntity versionEntity;
		try {
			appVersion = appVersionDao.getVersionPage(version);
			versionEntity = appVersion.getDataList().get(0);
		} catch (Exception e) {
			throw new BusinessException("该版本号不存在！");
		}
        if (null == versionEntity) {
        	throw new BusinessException("该版本号不存在！");
        }
        cacheManage.setNoExpire(APPVERSION, GsonUtils.toJson(versionEntity));
    }

}
