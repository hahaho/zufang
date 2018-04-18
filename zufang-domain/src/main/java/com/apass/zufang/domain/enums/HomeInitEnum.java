package com.apass.zufang.domain.enums;

public enum HomeInitEnum {
	
//	http://espapp.apass.cn/static/eshop/other/1520907719383.jpg
	//	http://espapp.apass.cn/static/eshop/other/1522233133300.jpg
	INIT_HOUSEIMG_1("1", "http://espapp.apass.cn/static/eshop/other/1522233133300.jpg"),// 首页初始img
	INIT_URL_1("3", "http://gfbapp.vcash.cn/v2/#/zufangUserRepay"),// 追加url
	INIT_TITLE_1("4", "用户还款指引"),
	
	
	INIT_HOUSEIMG_2("5", "http://espapp.apass.cn/static/eshop/other/1524045549871.jpg"),// 首页初始img
	INIT_URL_2("6", "http://ajqh.wap.apass.cn/#/downAppReg?channel=ajp"),// 追加url
	INIT_TITLE_2("7", "借款注册"),
	
	
	
	INIT_APARTIMG("2", "http://espapp.apass.cn/static/eshop/other/1520911924231.jpg");// 品牌公寓初始img;// 追加title
	
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
