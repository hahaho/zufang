package com.apass.zufang.domain.entity.rbac;

import com.apass.gfb.framework.annotation.MyBatisEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

/**
 * 
 * @description 菜单 
 * 
 * @author Listening
 * @version $Id: MenusDO.java, v 0.1 2014年10月25日 下午10:49:17 Listening Exp $
 */
@MyBatisEntity
public class MenusSettingDO {
    /**
     * 主键标识id
     */
    private Long               id;
    /**
     * 文本标题
     */
    private String               text;
    
    
    private String title;
    /**
     * 是否选中
     */
    @JsonIgnore
    private String               checkSign;
    
    private Boolean expand;
    /**
     * 父节点
     */
    private Long               parentId;
    /**
     * 子节点
     */
    private List<MenusSettingDO> children;

    public String getCheckSign() {
        return checkSign;
    }

    public void setCheckSign(String checkSign) {
        this.checkSign = checkSign;
    }

    public List<MenusSettingDO> getChildren() {
        return children;
    }

    public void setChildren(List<MenusSettingDO> children) {
        this.children = children;
    }

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

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Boolean isChecked() {
        return CollectionUtils.isEmpty(children) ? "Y".equals(checkSign) : null;
    }

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Boolean getExpand() {
		return expand;
	}

	public void setExpand(Boolean expand) {
		this.expand = expand;
	}
}
