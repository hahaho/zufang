package com.apass.zufang.domain.entity;

import java.util.Date;
import java.util.List;

public class WorkSubway {
    private Long id;

    private Long code;

    private String lineName;

    private String siteName;

    private String nearestPoint;

    private String level;

    private Long parentCode;

    private Date createdTime;

    private Date updatedTime;
    
    private List<WorkSubway> resultList;
    
    public List<WorkSubway> getResultList() {
		return resultList;
	}

	public void setResultList(List<WorkSubway> resultList) {
		this.resultList = resultList;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getNearestPoint() {
        return nearestPoint;
    }

    public void setNearestPoint(String nearestPoint) {
        this.nearestPoint = nearestPoint;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Long getParentCode() {
        return parentCode;
    }

    public void setParentCode(Long parentCode) {
        this.parentCode = parentCode;
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