package com.apass.zufang.domain.enums;

public enum EditFalgEnums {
	
	// 热门房源类型	1：正常，2:精选',
	EditFalg_0(0, "未编辑"),
	EditFalg_1(1, "编辑");
	
	private Integer code;

	private String message;
	
	private EditFalgEnums(Integer code, String message) {
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
}
