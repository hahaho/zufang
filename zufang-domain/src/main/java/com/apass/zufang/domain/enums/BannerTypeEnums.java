package com.apass.zufang.domain.enums;

import org.apache.commons.lang3.StringUtils;

public enum BannerTypeEnums {
	// 热门房源类型	1：正常，2:精选',
    TYPE_1("1", "首页"),
    TYPE_2("2", "品牌公寓");
	
	private String code;

	private String message;
	
	private BannerTypeEnums(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public static String getMessge(String code) {
		BannerTypeEnums[] banners = BannerTypeEnums.values();
		for (BannerTypeEnums banner : banners) {
			if(StringUtils.equals(banner.getCode(),code)){
				return banner.getMessage();
			}
		}
		return "";
	}
}
