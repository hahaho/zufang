package com.apass.zufang.domain.ajp.entity;


public class ZfangSearchEntity {
	
	/**
	 * 主键
	 */
	private  String id;
	/**
	 * 0:最近搜索 1:热门搜索
	 */
	private  String keyType;
	/**
	 * 内容
	 */
	private  String keyValue;
	/**
	 * 用户ID
	 */
	private  String userId;
	/**
	 * 0:可用 1:删除
	 */
	private  String keyStatus;
	/**
	 * 设备号
	 */
	private  String deviceId;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getKeyType() {
		return keyType;
	}
	public void setKeyType(String keyType) {
		this.keyType = keyType;
	}
	public String getKeyValue() {
		return keyValue;
	}
	public void setKeyValue(String keyValue) {
		this.keyValue = keyValue;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getKeyStatus() {
		return keyStatus;
	}
	public void setKeyStatus(String keyStatus) {
		this.keyStatus = keyStatus;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	
	
}
