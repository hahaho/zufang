package com.apass.zufang.domain.ajp.entity;

public class ZuFangLoginEntity {
	/**
	 * 用户ID
	 */
	private String customerId;
	
	/**
	 * 手机号
	 */
	private String mobile ;
	
	/**
	 * 租房密码
	 */
	private String zuFangPassword ;

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getZuFangPassword() {
		return zuFangPassword;
	}

	public void setZuFangPassword(String zuFangPassword) {
		this.zuFangPassword = zuFangPassword;
	}
	
	
}
