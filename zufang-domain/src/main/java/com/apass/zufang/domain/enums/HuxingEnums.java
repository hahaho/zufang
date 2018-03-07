package com.apass.zufang.domain.enums;
/**
 * 户型
 * @author zwd
 *
 */
public enum HuxingEnums {
	
	HUXING_0(0, "不限"),
    HUXING_1(1, "一室户"),
    HUXING_2(2, "二室户"),
    HUXING_3(3, "三室户"),
    HUXING_4(4, "四室户"),
    HUXING_MAX(5, "更大户型");
	
	private Integer code;

	private String message;
	
	private HuxingEnums(Integer code, String message) {
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
