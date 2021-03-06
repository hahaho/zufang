package com.apass.zufang.web.rbac;

import java.util.List;
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
import com.apass.gfb.framework.utils.BaseConstants.CommonCode;
import com.apass.gfb.framework.utils.GsonUtils;
import com.apass.gfb.framework.utils.RegExpUtils;
import com.apass.zufang.domain.Response;
import com.apass.zufang.domain.entity.rbac.MenusSettingDO;
import com.apass.zufang.domain.entity.rbac.PermissionsDO;
import com.apass.zufang.domain.entity.rbac.RolesDO;
import com.apass.zufang.service.rbac.RolesService;
import com.apass.zufang.utils.PaginationManage;
import com.apass.zufang.utils.ResponsePageBody;

/**
 * 
 * @description 角色管理
 *
 * @author lixining
 * @version $Id: UsersController.java, v 0.1 2016年6月22日 上午11:15:57 lixining Exp $
 */
@Path("/application/rbac/role")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class RolesController {
    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(RolesController.class);
    /**
     * role Service
     */
    @Autowired
    private RolesService rolesService;

    /**
     * 角色界面
     */
    @POST
    @Path("/page")
    public String handlePage() {
        return "rbac/roles-page";
    }

    /**
     * 角色列表JSON
     */
    @POST
    @Path("/pagelist")
    public ResponsePageBody<RolesDO> handlePageList(Map<String,String> paramMap) {
        ResponsePageBody<RolesDO> respBody = new ResponsePageBody<RolesDO>();
        try {
        	logger.info("handlePageList map--->{}",GsonUtils.toJson(paramMap));
            String pageNo = paramMap.get("page");
            String pageSize = paramMap.get( "rows");
            Integer pageNoNum = Integer.parseInt(pageNo);
            Integer pageSizeNum = Integer.parseInt(pageSize);
            Page page = new Page();
            page.setPage(pageNoNum <= 0 ? 1 : pageNoNum);
            page.setLimit(pageSizeNum <= 0 ? 1 : pageSizeNum);
            String roleCode = paramMap.get( "roleCode");
            String roleName = paramMap.get( "roleName");
            RolesDO paramDO = new RolesDO();
            paramDO.setRoleCode(roleCode);
            paramDO.setRoleName(roleName);
            PaginationManage<RolesDO> pagination = rolesService.page(paramDO, page);
            respBody.setTotal(pagination.getTotalCount());
            respBody.setRows(pagination.getDataList());
            respBody.setStatus(CommonCode.SUCCESS_CODE);
        } catch (Exception e) {
            logger.error("列表查询失败", e);
            respBody.setMsg("列表查询失败");
        }
        return respBody;
    }

    /**
     * 保存
     */
    @POST
    @Path("/save")
    public Response handleSave(Map<String,String> paramMap) {
        try {
        	logger.info("handleSave map--->{}",GsonUtils.toJson(paramMap));
            String roleId = paramMap.get("id");
            String roleCode = paramMap.get("roleCode");
            String roleName = paramMap.get("roleName");
            String description = paramMap.get("description");
            if (StringUtils.isAnyBlank(roleCode, roleName)) {
                return Response.fail("编码或角色名称不能为空");
            }
            if (!RegExpUtils.length(roleCode, 1, 100)) {
                return Response.fail("角色编码长度不合法");
            }
            if (!RegExpUtils.length(roleName, 1, 100)) {
                return Response.fail("角色名称长度不合法");
            }
            if (!RegExpUtils.length(description, 0, 200)) {
                return Response.fail("角色描述长度不合法");
            }
            RolesDO role = new RolesDO();
            if(StringUtils.isNotBlank(roleId)){
            	role.setId(Long.valueOf(roleId));
            }
            role.setRoleCode(roleCode);
            role.setRoleName(roleName);
            role.setDescription(description);
            // 删除角色记录 
            rolesService.save(role);
            return Response.success("success");
        } catch (BusinessException e) {
            logger.error(e.getErrorDesc(), e);
            return Response.fail(e.getErrorDesc());
        } catch (Exception e) {
            logger.error("保存角色失败", e);
            return Response.fail("保存角色记录失败");
        }
    }

    /**
     * 删除角色
     */
    @POST
    @Path("/delete")
    public Response handleDelete(Map<String,String> paramMap) {
        try {
        	logger.info("handleDelete map--->{}",GsonUtils.toJson(paramMap));
            String roleId = paramMap.get("roleId");
            if (StringUtils.isBlank(roleId)) {
                return Response.fail("角色ID不能为空");
            }
            // 删除角色记录 
            rolesService.delete(roleId);
            return Response.success("success");
        } catch (Exception e) {
            logger.error("删除角色失败", e);
            return Response.fail("删除角色记录失败");
        }
    }

    /**
     * 删除角色
     */
    @POST
    @Path("/load")
    public Response handleLoad(Map<String,String> paramMap) {
        try {
        	logger.info("handleLoad map--->{}",GsonUtils.toJson(paramMap));
            String roleId = paramMap.get("roleId");
            if (StringUtils.isBlank(roleId)) {
                return Response.fail("角色ID不能为空");
            }
            // 删除角色记录 
            RolesDO rolesDO = rolesService.select(Long.valueOf(roleId));
            if (rolesDO == null) {
                throw new BusinessException("角色记录不存在,请刷新列表后重试");
            }
            return Response.success("success", rolesDO);
        } catch (BusinessException e) {
            logger.error(e.getErrorDesc(), e);
            return Response.fail(e.getErrorDesc());
        } catch (Exception e) {
            logger.error("查询角色失败", e);
            return Response.fail("查询角色记录失败");
        }
    }

    /**
     * 加载角色菜单设置
     */
    @POST
    @Path("/load/rolemenu/settings")
    public Response handleLoadRoleMenuSettings(Map<String,String> paramMap) {
        try {
        	logger.info("handleLoadRoleMenuSettings map--->{}",GsonUtils.toJson(paramMap));
            String roleId = paramMap.get("roleId");
            List<MenusSettingDO> menuList = rolesService.selectRoleMenuSettings(roleId);
            for (MenusSettingDO menu : menuList) {
				menu.setTitle(menu.getText());
				menu.setExpand(true);
			}
            return Response.success("success", menuList);
        } catch (Exception e) {
            logger.error("加载角色菜单失败", e);
            return Response.fail("加载角色菜单失败");
        }
    }

    /**
     * 保存角色菜单设置
     */
    @POST
    @Path("/save/rolemenu/settings")
    public Response handleSaveRoleMenuSettings(Map<String,String> paramMap) {
        try {
        	logger.info("handleSaveRoleMenuSettings map--->{}",GsonUtils.toJson(paramMap));
            String roleId = paramMap.get("roleId");
            String menus = paramMap.get("menus");
            if (StringUtils.isBlank(roleId)) {
                return Response.fail("角色ID不能为空");
            }
            rolesService.saveRoleMenuSettings(roleId, menus);
            return Response.success("success");
        } catch (Exception e) {
            logger.error("角色菜单保存失败", e);
            return Response.fail("角色菜单设置失败");
        }
    }

    /**
     * 角色资源可用列表
     */
    @POST
    @Path("/load/available/permissions")
    public Response handleLoadAvailablePermissions(Map<String,String> paramMap) {
        try {
        	logger.info("handleLoadAvailablePermissions map--->{}",GsonUtils.toJson(paramMap));
            String roleId = paramMap.get( "roleId");
            if (StringUtils.isBlank(roleId)) {
                return null;
            }
            List<PermissionsDO> permissionList = rolesService.loadAvailablePermissions(roleId);
            return Response.success("success", permissionList);
        } catch (Exception e) {
            logger.error("加载可分配资源失败", e);
            return Response.fail("加载可分配资源失败");
        }
    }

    /**
     * 角色资源已分配列表
     */
    @POST
    @Path("/load/assigned/permissions")
    public Response handleLoadAssignedPermissions(Map<String,String> paramMap) {
        try {
        	logger.info("handleLoadAssignedPermissions map--->{}",GsonUtils.toJson(paramMap));
            String roleId = paramMap.get("roleId");
            if (StringUtils.isBlank(roleId)) {
                return null;
            }
            List<PermissionsDO> permissionList = rolesService.loadAssignedPermissions(roleId);
            return Response.success("success", permissionList);
        } catch (Exception e) {
            logger.error("加载已分配资源失败", e);
            return Response.fail("加载已分配资源失败");
        }
    }

    /**
     * 保存资源列表
     */
    @POST
    @Path("/save/assigned/permissions")
    public Response handleSaveAssignedPermissions(Map<String,String> paramMap) {
        try {
        	logger.info("handleSaveAssignedPermissions map--->{}",GsonUtils.toJson(paramMap));
            String roleId = paramMap.get( "roleId");
            String permissions = paramMap.get( "permissions");
            if (StringUtils.isBlank(roleId)) {
                return Response.fail("角色ID不能为空");
            }
            rolesService.saveAssignedPermissions(roleId, permissions);
            return Response.success("success");
        } catch (Exception e) {
            logger.error("保存资源分配记录失败", e);
            return Response.fail("保存资源分配记录失败");
        }
    }
}
