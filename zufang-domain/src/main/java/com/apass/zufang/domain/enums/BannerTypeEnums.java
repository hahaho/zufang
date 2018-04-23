package com.apass.zufang.domain.enums;

public enum BannerTypeEnums {
	// 热门房源类型	1：正常，2:精选',
    TYPE_1(1, "首页"),
    TYPE_2(2, "品牌公寓");
	
	private Integer code;

	private String message;
	
	private BannerTypeEnums(Integer code, String message) {
		this.code = code;
		this.message = message;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public static String getMessge(Integer code) {
		BannerTypeEnums[] banners = BannerTypeEnums.values();
		for (BannerTypeEnums banner : banners) {
			if(banner.getCode() == code){
				return banner.getMessage();
			}
		}
		return "";
	}
}
