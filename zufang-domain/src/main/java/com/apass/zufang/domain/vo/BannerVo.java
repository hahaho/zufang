package com.apass.zufang.domain.vo;

import com.apass.zufang.domain.enums.BannerTypeEnums;

public class BannerVo {
	
	private String id;
	
	private String activityUrl;
	
	private String name;
	
	private String type;
	
	private String sort;
	
	private String typeMsg;
	
	private String imgUrl;
	
	private String fullImgUrl;
	
	private String operationName;
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getTypeMsg() {
		return BannerTypeEnums.getMessge(this.type);
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	public String getFullImgUrl() {
		return fullImgUrl;
	}

	public void setFullImgUrl(String fullImgUrl) {
		this.fullImgUrl = fullImgUrl;
	}
	
}
