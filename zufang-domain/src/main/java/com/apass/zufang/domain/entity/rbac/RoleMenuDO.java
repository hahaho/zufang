package com.apass.zufang.domain.entity.rbac;

import com.apass.gfb.framework.annotation.MyBatisEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

/**
 * 
 * @description CRM权限管理-角色权限设置DO实体类
 * 
 * @author Listening
 * @version $Id: RolePermissionDO.java, v 0.1 2014-11-22 15:22:36 Listening Exp $
 */
@MyBatisEntity
public class RoleMenuDO {
    /**
     * ID
     */
    private Long id;
    /**
     * 角色ID
     */
    private Long roleId;
    /**
     * 菜单ID
     */
    private Long menuId;
    /**
     * 创建人
     */
    @JsonIgnore
    private String createdBy;
    /**
     * 更新人
     */
    @JsonIgnore
    private String updatedBy;
    /**
     * 创建日期
     */
    @JsonIgnore
    private Date   createdTime;
    /**
     * 更新日期
     */
    @JsonIgnore
    private Date   updatedTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }
}
