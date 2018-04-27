package com.apass.zufang.domain.enums;
import org.apache.commons.lang3.StringUtils;
/**
 * t_zfang_reserve_house  reserve_status 预约状态枚举
 * @author Administrator
 *
 */
public enum ReserveStatusEnums {
	APPOINTMENT_1("1", "已预约"),
    CHANGE_2("2", "已变更"),
    CANCEL_3("3", "已取消"),
    PAST_4("4", "已过期");
	private String code;
	private String message;
	private ReserveStatusEnums(String code, String message) {
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
	public static String getMessge(String code) {
		ReserveStatusEnums[] reserveStatus = ReserveStatusEnums.values();
		for (ReserveStatusEnums status : reserveStatus) {
			if(StringUtils.equals(status.getCode(),code)){
				return status.getMessage();
			}
		}
		return "";
	}
}