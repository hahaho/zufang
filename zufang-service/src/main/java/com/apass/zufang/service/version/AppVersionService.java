package com.apass.zufang.service.version;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apass.gfb.framework.cache.CacheManager;
import com.apass.gfb.framework.exception.BusinessException;
import com.apass.gfb.framework.utils.BaseConstants;
import com.apass.gfb.framework.utils.GsonUtils;
import com.apass.zufang.domain.dto.AppVersionQueryParams;
import com.apass.zufang.domain.entity.AppVersionEntity;
import com.apass.zufang.mapper.zfang.AppVersionMapper;
import com.apass.zufang.utils.ResponsePageBody;

@Service
public class AppVersionService {

    private static final String APPVERSION = "APP_VERSION_CODE";

    @Autowired
    private AppVersionMapper appVersionDao;
    @Autowired
    private CacheManager cacheManage;

    public ResponsePageBody<AppVersionEntity> getVersionPage(AppVersionQueryParams version) {
    	  ResponsePageBody<AppVersionEntity> pageBody = new ResponsePageBody<AppVersionEntity>();
    	  Integer count = appVersionDao.getVersionPageCount(version);
    	  List<AppVersionEntity> list = appVersionDao.getVersionPage(version);
    	  
        String isReleasedJson = cacheManage.get(APPVERSION);
        if(!StringUtils.isEmpty(isReleasedJson)){
            AppVersionEntity releasedVer = GsonUtils.convertObj(isReleasedJson, AppVersionEntity.class);
            for (AppVersionEntity ave : list) {
                if (ave.getId()==releasedVer.getId()) {
                    ave.setReleased(true);
                }
            }
        }
        pageBody.setTotal(count);
        pageBody.setRows(list);
        pageBody.setStatus(BaseConstants.CommonCode.SUCCESS_CODE);
        return pageBody;
    }


    public void save(AppVersionEntity appVersion) {
        appVersionDao.insert(appVersion);
    }

    public void deployVersion(AppVersionQueryParams version) throws BusinessException {
    	
        List<AppVersionEntity> appVersion;
		AppVersionEntity versionEntity;
		try {
			appVersion = appVersionDao.getVersionPage(version);
			versionEntity = appVersion.get(0);
		} catch (Exception e) {
			throw new BusinessException("该版本号不存在！");
		}
        if (null == versionEntity) {
        	throw new BusinessException("该版本号不存在！");
        }
        cacheManage.set(APPVERSION, GsonUtils.toJson(versionEntity));
    }

}
