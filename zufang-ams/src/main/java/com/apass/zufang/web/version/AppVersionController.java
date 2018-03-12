package com.apass.zufang.web.version;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.apass.gfb.framework.mybatis.page.Pagination;
import com.apass.gfb.framework.utils.BaseConstants;
import com.apass.gfb.framework.utils.HttpWebUtils;
import com.apass.zufang.domain.Response;
import com.apass.zufang.domain.common.AppVersionEntity;
import com.apass.zufang.service.version.AppVersionService;
import com.apass.zufang.utils.ResponsePageBody;

@Controller
@RequestMapping(value = "/zf/appversion")
public class AppVersionController {
    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AppVersionController.class);

    @Autowired
    private AppVersionService appVersionService;

    @RequestMapping("/page")
    public String activityPage() {
        return "ajp/appversionList";
    }

    @ResponseBody
    @RequestMapping("/query")
    public ResponsePageBody<AppVersionEntity> queryAppVersionList(HttpServletRequest request) {
        ResponsePageBody<AppVersionEntity> respBody = new ResponsePageBody<AppVersionEntity>();
        try {
            String pageNo = HttpWebUtils.getValue(request, "page");
            String pageSize = HttpWebUtils.getValue(request, "rows");
            String versionName = HttpWebUtils.getValue(request, "versionName");
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

    @RequestMapping("/saveVersion")
    @ResponseBody
    public Response queryDetilById(AppVersionEntity app) {
        if (StringUtils.isEmpty(app.getVersionName())) {
            return Response.fail("android versionName 不能为空");
        }
        if (StringUtils.isEmpty(app.getIosVersionname())) {
            return Response.fail("IOS versionName 不能为空");
        }

        if (StringUtils.isEmpty(app.getVersionCode())) {
            return Response.fail("android versioncode 不能为空");
        }

        if (StringUtils.isEmpty(app.getIosVersioncode())) {
            return Response.fail("IOS versioncode 不能为空");
        }

        if (StringUtils.isEmpty(app.getAppSize())) {
            return Response.fail("android appSize不能为空");
        }

        if (StringUtils.isEmpty(app.getIosAppsize())) {
            return Response.fail("IOS appSize不能为空");
        }

        if (StringUtils.isEmpty(app.getExplains())) {
            return Response.fail("android explain值不能为空");
        }

        if (StringUtils.isEmpty(app.getIosExplains())) {
            return Response.fail("Ios explain值不能为空");
        }

        if (!"0".equals(app.getUpgradeflag()) && !"1".equals(app.getUpgradeflag())) {
            return Response.fail("android upgradeFlag 值非法");
        }
        if (!"0".equals(app.getIosUpgradeflag()) && !"1".equals(app.getIosUpgradeflag())) {
            return Response.fail("Ios upgradeFlag 值非法");
        }

        if (StringUtils.isEmpty(app.getDistribution())) {
            return Response.fail("android distribution值不能为空");
        }

        if (StringUtils.isEmpty(app.getIosDistribution())) {
            return Response.fail("Ios distribution值不能为空");
        }

        if (StringUtils.isEmpty(app.getFileRoute())) {
            return Response.fail("fileRoute内容不能为空");
        }

        if (StringUtils.isAnyEmpty(app.getDownloanurl(), app.getIndexbanner(), app.getMybanner())) {
            return Response.fail("downloadurl 或 indexBanner 或 mybanner 值不能为空");
        }
        try {
            appVersionService.save(app);
        } catch (Exception e) {
            LOGGER.error("queryDetail error", e);
            return  Response.fail("新增版本失败");
        }
        return Response.success("新增版本成功");
    }

    @RequestMapping("/deployVersion")
    @ResponseBody
    public Response deployVersion(String versionId) {
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
