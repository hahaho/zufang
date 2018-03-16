package com.apass.zufang.domain.entity.rbac;

import com.apass.gfb.framework.annotation.MyBatisEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

/**
 * 
 * @description CRM权限管理-权限资源DO实体类
 * 
 * @author Listening
 * @version $Id: PermissionsDO.java, v 0.1 2014-11-15 22:28:28 Listening Exp $
 */
@MyBatisEntity
public class PermissionsDO {
    /**
     * ID
     */
    private Long id;
    /**
     * 权限编码
     */
    private String permissionCode;
    /**
     * 权限名称
     */
    private String permissionName;
    /**
     * 描述
     */
    private String description;
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

    // not id
    private Long neId;

    public Long getNeId() {
        return neId;
    }

    public void setNeId(Long neId) {
        this.neId = neId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPermissionCode() {
        return permissionCode;
    }

    public void setPermissionCode(String permissionCode) {
        this.permissionCode = permissionCode;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
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
