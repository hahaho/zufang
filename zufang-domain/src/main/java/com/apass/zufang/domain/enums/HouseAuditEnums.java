package com.apass.zufang.domain.enums;

public enum HouseAuditEnums {
	// 房源审核类型	1：通过，2:拒绝',
	HOUSE_AUDIT_0("0", "通过"),
	HOUSE_AUDIT_1("1", "拒绝");
	
	private String code;

	private String message;
	
	private HouseAuditEnums(String code, String message) {
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
