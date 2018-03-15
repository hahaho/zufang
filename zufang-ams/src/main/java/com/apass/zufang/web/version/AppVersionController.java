package com.apass.zufang.web.version;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.apass.gfb.framework.mybatis.page.Pagination;
import com.apass.gfb.framework.utils.BaseConstants;
import com.apass.gfb.framework.utils.CommonUtils;
import com.apass.gfb.framework.utils.HttpWebUtils;
import com.apass.zufang.domain.Response;
import com.apass.zufang.domain.entity.AppVersionEntity;
import com.apass.zufang.service.version.AppVersionService;
import com.apass.zufang.utils.ResponsePageBody;

@Path("/zf/appversion")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class AppVersionController {
    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AppVersionController.class);

    @Autowired
    private AppVersionService appVersionService;

    @GET
	@Path("/init")
    public String activityPage() {
        return "ajp/appversionList";
    }

    @POST
	@Path("/query")
    public ResponsePageBody<AppVersionEntity> queryAppVersionList(Map<String,Object> paramMap) {
    	

        ResponsePageBody<AppVersionEntity> respBody = new ResponsePageBody<AppVersionEntity>();
        try {
        	String pageNo = CommonUtils.getValue(paramMap, "page");
        	String pageSize = CommonUtils.getValue(paramMap, "pageSize");
        	String versionName = CommonUtils.getValue(paramMap, "versionName");
        	
            if (StringUtils.isEmpty(pageNo)) {
                pageNo = 1 + "";
            }
            if (StringUtils.isEmpty(pageSize)) {
                pageSize = 10 + "";
            }

            AppVersionEntity version = new AppVersionEntity();
            if (null != versionName && !versionName.trim().isEmpty()) {
                version.setVersionName(versionName);
            }

            version.setPage(Integer.parseInt(pageNo));
            version.setRows(Integer.parseInt(pageSize));

            Pagination<AppVersionEntity> pagination = appVersionService.getVersionPage(version);

            if (pagination == null) {
                respBody.setTotal(0);
                respBody.setStatus(BaseConstants.CommonCode.SUCCESS_CODE);
                return respBody;
            }
            respBody.setTotal(pagination.getTotalCount());
            respBody.setRows(pagination.getDataList());
            respBody.setStatus(BaseConstants.CommonCode.SUCCESS_CODE);
        } catch (Exception e) {
            LOGGER.error("app版本号查询失败", e);
            respBody.setStatus(BaseConstants.CommonCode.FAILED_CODE);
            respBody.setMsg("app版本号查询失败");
        }
        return respBody;
    }

	@POST
	@Path("/saveVersion")
    public Response queryDetilById(Map<String,Object> paramMap) {
		
		String versionName = CommonUtils.getValue(paramMap, "versionName");
		String versionCode = CommonUtils.getValue(paramMap, "versionCode");
		String fileRoute = CommonUtils.getValue(paramMap, "fileRoute");
		String appSize = CommonUtils.getValue(paramMap, "appSize");
		String upgradeflag = CommonUtils.getValue(paramMap, "upgradeflag");
		String explains = CommonUtils.getValue(paramMap, "explains");
		String distribution = CommonUtils.getValue(paramMap, "distribution");
		String iosVersionname = CommonUtils.getValue(paramMap, "iosVersionname");
		String iosVersioncode = CommonUtils.getValue(paramMap, "iosVersioncode");
		String iosAppsize = CommonUtils.getValue(paramMap, "iosAppsize");
		String iosUpgradeflag = CommonUtils.getValue(paramMap, "iosUpgradeflag");
		String iosExplains = CommonUtils.getValue(paramMap, "iosExplains");
		String iosDistribution = CommonUtils.getValue(paramMap, "iosDistribution");
		String downloanurl = CommonUtils.getValue(paramMap, "downloanurl");
		String indexbanner = CommonUtils.getValue(paramMap, "indexbanner");
		String mybanner = CommonUtils.getValue(paramMap, "mybanner");
	
        if (StringUtils.isEmpty(versionName)) {
            return Response.fail("android versionName 不能为空");
        }
        if (StringUtils.isEmpty(iosVersionname)) {
            return Response.fail("IOS versionName 不能为空");
        }

        if (StringUtils.isEmpty(versionCode)) {
            return Response.fail("android versioncode 不能为空");
        }

        if (StringUtils.isEmpty(iosVersioncode)) {
            return Response.fail("IOS versioncode 不能为空");
        }

        if (StringUtils.isEmpty(appSize)) {
            return Response.fail("android appSize不能为空");
        }

        if (StringUtils.isEmpty(iosAppsize)) {
            return Response.fail("IOS appSize不能为空");
        }

        if (StringUtils.isEmpty(explains)) {
            return Response.fail("android explain值不能为空");
        }

        if (StringUtils.isEmpty(iosExplains)) {
            return Response.fail("Ios explain值不能为空");
        }

        if (!"0".equals(iosUpgradeflag) && !"1".equals(iosUpgradeflag)) {
            return Response.fail("android upgradeFlag 值非法");
        }
        if (!"0".equals(iosUpgradeflag) && !"1".equals(iosUpgradeflag)) {
            return Response.fail("Ios upgradeFlag 值非法");
        }

        if (StringUtils.isEmpty(distribution)) {
            return Response.fail("android distribution值不能为空");
        }

        if (StringUtils.isEmpty(iosDistribution)) {
            return Response.fail("Ios distribution值不能为空");
        }

        if (StringUtils.isEmpty(fileRoute)) {
            return Response.fail("fileRoute内容不能为空");
        }
        
        if (StringUtils.isAnyEmpty(downloanurl, indexbanner, mybanner)) {
            return Response.fail("downloadurl 或 indexBanner 或 mybanner 值不能为空");
        }
        AppVersionEntity app=new AppVersionEntity();
        app.setVersionName(versionName);
        app.setVersionCode(versionCode);
        app.setFileRoute(fileRoute);
        app.setAppSize(appSize);
        app.setUpgradeflag(upgradeflag);
        app.setExplains(explains);
        app.setDistribution(distribution);
        app.setIosVersionname(iosVersionname);
        app.setIosVersioncode(iosVersioncode);
        app.setIosAppsize(iosAppsize);
        app.setIosUpgradeflag(iosUpgradeflag);
        app.setIosExplains(iosExplains);
        app.setIosDistribution(iosDistribution);
        app.setDownloanurl(downloanurl);
        app.setIndexbanner(indexbanner);
        app.setMybanner(mybanner);
        try {
            appVersionService.save(app);
        } catch (Exception e) {
            LOGGER.error("queryDetail error", e);
            return  Response.fail("新增版本失败");
        }
        return Response.success("新增版本成功");
    }

    @POST
	@Path("/deployVersion")
    public Response deployVersion(Map<String,Object> paramMap) {
    	String versionId = CommonUtils.getValue(paramMap, "versionId");
        if (StringUtils.isEmpty(versionId)) {
            return Response.fail("请选择需要发布的版本");
        }
        try {
        	AppVersionEntity version = new AppVersionEntity();
        	version.setId(Long.valueOf(versionId));
            appVersionService.deployVersion(version);
        } catch (Exception e) {
            LOGGER.error("queryDetail error", e);
            Response.success("版本发布失败");
        }
        return Response.success("版本发布成功");
    }
}
