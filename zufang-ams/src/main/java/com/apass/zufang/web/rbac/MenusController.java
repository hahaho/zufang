package com.apass.zufang.web.rbac;

import java.net.URLDecoder;
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
import com.apass.gfb.framework.utils.CommonUtils;
import com.apass.gfb.framework.utils.RegExpUtils;
import com.apass.zufang.domain.Response;
import com.apass.zufang.domain.entity.rbac.MenusDO;
import com.apass.zufang.service.rbac.MenusService;

/**
 * 
 * @description 菜单管理
 *
 * @author lixining
 * @version $Id: MenusController.java, v 0.1 2016年6月22日 上午11:15:57 lixining Exp $
 */
@Path("/application/rbac/menu")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class MenusController {
    /**
     * 日志
     */
    private static final Logger LOG = LoggerFactory.getLogger(MenusController.class);
    /**
     * Menu Service
     */
    @Autowired
    private MenusService menusService;

    /**
     * 菜單页面加载
     */
    @POST
    @Path("/page")
    public String handlePage() {
        return "rbac/menus-page";
    }

    /**
     * 菜单Tree数据JSON
     */
    @POST
    @Path("/treejson")
    public Response handleTreeJson(Map<String,String> paramMap) {
        try {
            List<MenusDO> menuList = menusService.selectPermitTreeJson();
            return Response.success("success", menuList);
        } catch (Exception e) {
            LOG.error("load tree json fail", e);
            return Response.fail("菜单加载失败");
        }
    }

    /**
     * 菜单维护列表
     */
    @POST
    @Path("/pagelist")
    public Response handlePageList(Map<String,String> paramMap) {
        try {
            String menuName = paramMap.get("menuName");
            if (StringUtils.isNoneBlank(menuName)) {
                menuName = URLDecoder.decode(menuName, "utf-8");
            }

            List<MenusDO> menuList = menusService.selectAllMenuTreeJson(menuName);
            return Response.success("success", menuList);
        } catch (Exception e) {
            LOG.error("load menu fail", e);
            return Response.fail("菜单加载失败");
        }
    }

    /**
     * 保存数据
     */
    @POST
    @Path("/save")
    public Response handleSave(Map<String,String> paramMap) {
        try {
            String menuId = paramMap.get("id");
            String text = paramMap.get("text");
            String url = paramMap.get("url");
            String display = paramMap.get("display");
            String parentId = paramMap.get("parentId");
            if (!RegExpUtils.length(text, 1, 50)) {
                return Response.fail("菜单名称长度不合法");
            }
            if (!RegExpUtils.length(url, 0, 100)) {
                return Response.fail("链接地址长度不合法");
            }
            if (!RegExpUtils.length(parentId, 1, 50)) {
                return Response.fail("父节点ID长度不合法");
            }
            if (!StringUtils.isNumeric(display) || CommonUtils.getInt(display) <= 0) {
                return Response.fail("显示顺序必须为非0正整数");
            }
            MenusDO menusDO = new MenusDO();
            if(StringUtils.isNotEmpty(menuId)){
                menusDO.setId(Long.valueOf(menuId));
            }
            menusDO.setDisplay(Integer.parseInt(display));
            menusDO.setIconCls("");
            menusDO.setParentId(Long.valueOf(parentId));
            menusDO.setText(text);
            menusDO.setUrl(url);
            menusService.save(menusDO);
            return Response.success("success");
        } catch (BusinessException e) {
            LOG.error(e.getErrorDesc(), e);
            return Response.fail(e.getErrorDesc());
        } catch (Exception e) {
            LOG.error("保存菜单失败", e);
            return Response.fail("保存菜单记录失败");
        }
    }

    /**
     * 删除数据
     */
    @POST
    @Path("/delete")
    public Response handleDelete(Map<String,String> paramMap) {
        try {
            String menuId = paramMap.get("menuId");
            if (StringUtils.isBlank(menuId)) {
                return Response.fail("菜单ID不能为空");
            }
            // 删除菜单记录 
            menusService.delete(Long.valueOf(menuId));
            return Response.success("success");
        } catch (Exception e) {
            LOG.error("删除菜单失败", e);
            return Response.fail("删除菜单记录失败");
        }
    }

    /**
     * 加载数据
     */
    @POST
    @Path("/load")
    public Response handleLoad(Map<String,String> paramMap) {
        try {
            String menuId = paramMap.get("menuId");
            if (StringUtils.isBlank(menuId)) {
                return Response.fail("菜单ID不能为空");
            }
            MenusDO menu = menusService.select(Long.valueOf(menuId));
            if (menu == null) {
                throw new BusinessException("菜单记录不存在, 请刷新列表后重试");
            }
            return Response.success("success", menu);
        } catch (BusinessException e) {
            LOG.error(e.getErrorDesc(), e);
            return Response.fail(e.getErrorDesc());
        } catch (Exception e) {
            LOG.error("加载菜单失败", e);
            return Response.fail("加载菜单记录失败");
        }
    }

}
