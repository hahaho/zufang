package com.apass.zufang.domain.entity.rbac;

import com.apass.gfb.framework.annotation.MyBatisEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;
import java.util.List;

/**
 * 
 * @description 菜单 
 * 
 * @author Listening
 * @version $Id: MenusDO.java, v 0.1 2014年10月25日 下午10:49:17 Listening Exp $
 */
@MyBatisEntity
public class MenusDO {
    /**
     * 主键标识id
     */
    private Long        id;
    /**
     * 文本标题
     */
    private String        text;

    private String title;
    /**
     * 图标样式
     */
    private String        iconCls;
    /**
     * 链接地址
     */
    private String        url;
    /**
     * 父节点
     */
    private Long        parentId;
    /**
     * 显示顺序
     */
    private Integer       display;
    /**
     * 状态
     */
    private String        state = "opened";
    /**
     * 菜单节点
     */
    private List<MenusDO> children;

    /**
     * 查询参数
     */
    @JsonIgnore
    private Long        neId;
    /**
     * 创建人
     */
    @JsonIgnore
    private String        createdBy;
    /**
     * 更新人
     */
    @JsonIgnore
    private String        updatedBy;
    /**
     * 创建日期
     */
    @JsonIgnore
    private Date          createdTime;
    /**
     * 更新日期
     */
    @JsonIgnore
    private Date          updatedTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getIconCls() {
        return iconCls;
    }

    public void setIconCls(String iconCls) {
        this.iconCls = iconCls;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getDisplay() {
        return display;
    }

    public void setDisplay(Integer display) {
        this.display = display;
    }

    public List<MenusDO> getChildren() {
        return children;
    }

    public void setChildren(List<MenusDO> children) {
        this.children = children;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Long getNeId() {
        return neId;
    }

    public void setNeId(Long neId) {
        this.neId = neId;
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

    public String getTitle() {
        return text;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
