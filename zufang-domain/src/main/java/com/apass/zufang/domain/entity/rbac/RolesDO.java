package com.apass.zufang.domain.entity.rbac;

import com.apass.gfb.framework.annotation.MyBatisEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

/**
 * 
 * @description CRM角色管理DO实体类
 * 
 * @author Listening
 * @version $Id: RolesDO.java, v 0.1 2014-11-08 14:22:43 Listening Exp $
 */
@MyBatisEntity
public class RolesDO {
    /**
     * ID
     */
    private Long id;
    /**
     * 角色编码
     */
    private String roleCode;
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 描述
     */
    private String description;
    /**
     * 创建人
     */
    private String createdBy;
    /**
     * 更新人
     */
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
    // ne id
    private Long neId;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getNeId() {
        return neId;
    }

    public void setNeId(Long neId) {
        this.neId = neId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
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
