package com.apass.zufang.domain.vo;

import com.apass.zufang.domain.enums.BannerTypeEnums;

public class BannerVo {
	
	private Long id;
	
	private String activityUrl;
	
	private String name;
	
	private Integer type;
	
	private Long sort;
	
	private String typeMsg;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getActivityUrl() {
		return activityUrl;
	}

	public void setActivityUrl(String activityUrl) {
		this.activityUrl = activityUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Long getSort() {
		return sort;
	}

	public void setSort(Long sort) {
		this.sort = sort;
	}

	public String getTypeMsg() {
		return BannerTypeEnums.getMessge(this.type);
	}
	
}
