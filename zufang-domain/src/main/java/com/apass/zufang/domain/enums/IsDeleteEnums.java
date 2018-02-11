package com.apass.zufang.domain.enums;

public enum IsDeleteEnums {
	
	// 热门房源类型	1：正常，2:精选',
    IS_DELETE_00("00", "正常"),
    IS_DELETE_01("01", "删除");
	
	private String code;

	private String message;
	
	private IsDeleteEnums(String code, String message) {
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
}
