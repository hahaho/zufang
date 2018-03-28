package com.apass.zufang.domain.enums;

public enum HomeInitEnum {
	
	INIT_HOUSEIMG("1", "http://espapp.apass.cn/static/eshop/other/1520907719383.jpg"),// 首页初始img
	INIT_APARTIMG("2", "http://espapp.apass.cn/static/eshop/other/1520911924231.jpg"),// 品牌公寓初始img
	INIT_URL("3", "http://gfbapp.vcash.cn/v2/#/zufangUserRepay"),// 追加url
	INIT_TITLE("4", "用户还款指引");// 追加title
	
    private String code;
	
	private String message;

	private HomeInitEnum(String code, String message) {
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
