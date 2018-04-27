package com.apass.zufang.search.enums;

public enum SortFieldEnum {

	SORTFIE_DEFAULT("default", "默认"),

	SORTFIE_NEWESET("neweset", "最新发布"),

	SORTFIE_PRICE("price", "价格");

	private String field;

	private String message;

	private SortFieldEnum(String field, String message) {
		this.field = field;
		this.message = message;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}



}
