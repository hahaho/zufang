package com.apass.zufang.web.rbac;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.apass.gfb.framework.exception.BusinessException;
import com.apass.gfb.framework.mybatis.page.Page;
import com.apass.gfb.framework.security.toolkit.SpringSecurityUtils;
import com.apass.gfb.framework.utils.CommonUtils;
import com.apass.gfb.framework.utils.GsonUtils;
import com.apass.gfb.framework.utils.RegExpUtils;
import com.apass.zufang.domain.Response;
import com.apass.zufang.domain.entity.rbac.PermissionsDO;
import com.apass.zufang.service.rbac.PermissionsService;
import com.apass.zufang.utils.ResponsePageBody;
/**
 * 资源管理
 * @author Administrator
 *
 */
@Path("/rbac/permissionsController")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class PermissionsController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PermissionsController.class);
    @Autowired
    private PermissionsService permissionsService;
    /**
     * INIT
     * @return
     */
    @POST
    @Path("/init")
    public String init() {
        return "rbac/permissionsmanage";
    }
    /**
     * 资源列表 分页查询
     * @param map
     * @return
     */
    @POST
    @Path("/getPermissionsList")
    public ResponsePageBody<PermissionsDO> getPermissionsList(Map<String,Object> map) {
    	ResponsePageBody<PermissionsDO> respBody = new ResponsePageBody<PermissionsDO>();
        try {
        	LOGGER.info("getPermissionsList map--->{}",GsonUtils.toJson(map));
        	String permissionName = CommonUtils.getValue(map, "permissionName");
        	String permissionCode = CommonUtils.getValue(map, "permissionCode");
        	String page = CommonUtils.getValue(map, "page");
        	String rows = CommonUtils.getValue(map, "rows");
            PermissionsDO paramDO = new PermissionsDO();
            if(StringUtils.isNotBlank(permissionName)){
            	paramDO.setPermissionName(permissionName);
            }
            if(StringUtils.isNotBlank(permissionCode)){
            	paramDO.setPermissionCode(permissionCode);
            }
            Page p = new Page();
            p.setPage(Integer.parseInt(page));
            p.setLimit(Integer.parseInt(rows));
            respBody = permissionsService.getPermissionsList(paramDO,p);
        } catch (Exception e) {
            LOGGER.error("getPermissionsList EXCEPTION --- --->{}", e);
            respBody.setMsg("资源信息列表查询失败");
        }
        return respBody;
    }
    /**
     * 保存资源
     * @param map
     * @return
     */
    @POST
    @Path("/savePermission")
    public Response savePermission(Map<String,String> map) {
        try {
            String permissionId = map.get("id");
            String permissionCode = map.get("permissionCode");
            String permissionName = map.get("permissionName");
            String description = map.get("description");
            if (StringUtils.isAnyBlank(permissionCode, permissionName)) {
                return Response.fail("资源编码或资源名称不能为空");
            }
            if (!RegExpUtils.length(permissionCode, 1, 100)) {
                return Response.fail("资源编码长度不合法");
            }
            if (!RegExpUtils.length(permissionName, 1, 100)) {
                return Response.fail("资源名称长度不合法");
            }
            if (!RegExpUtils.length(description, 0, 200)) {
                return Response.fail("资源描述长度不合法");
            }
            PermissionsDO entity = new PermissionsDO();
            entity.setPermissionCode(permissionCode);
            entity.setPermissionName(permissionName);
            entity.setDescription(description);
            String user = SpringSecurityUtils.getCurrentUser();
            return permissionsService.savePermission(permissionId,entity,user);
        } catch (BusinessException e) {
            LOGGER.error(e.getErrorDesc(), e);
            return Response.fail(e.getErrorDesc());
        } catch (Exception e) {
            LOGGER.error("保存资源失败", e);
            return Response.fail("保存资源失败!");
        }
    }
    /**
     * 删除资源
     * @param map
     * @return
     */
    @POST
    @Path("/delete")
    public Response handleDelete(Map<String,String> map) {
        try {
            String permissionId = map.get("permissionId");
            if (StringUtils.isBlank(permissionId)) {
                return Response.fail("资源ID不能为空");
            }
            permissionsService.delete(Long.valueOf(permissionId));
            return Response.success("success");
        } catch (Exception e) {
            LOGGER.error("删除资源失败", e);
            return Response.fail("删除资源记录失败");
        }
    }
    /**
     * 查询资源
     * @param map
     * @return
     */
    @POST
    @Path("/load")
    public Response handleLoad(Map<String,String> map) {
        try {
            String permissionId = map.get("permissionId");
            if (StringUtils.isBlank(permissionId)) {
                return Response.fail("资源ID不能为空");
            }
            PermissionsDO permission = permissionsService.select(Long.valueOf(permissionId));
            if (permission == null) {
                throw new BusinessException("资源记录不存在,请刷新列表后重试");
            }
            return Response.success("success", permission);
        } catch (BusinessException e) {
            LOGGER.error(e.getErrorDesc(), e);
            return Response.fail(e.getErrorDesc());
        } catch (Exception e) {
            LOGGER.error("加载资源失败", e);
            return Response.fail("加载资源记录失败");
        }
    }
}