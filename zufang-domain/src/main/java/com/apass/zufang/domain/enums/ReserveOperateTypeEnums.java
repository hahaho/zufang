package com.apass.zufang.domain.enums;
import org.apache.commons.lang3.StringUtils;
/**
 * t_zfang_reserve_record  operate_type 记录变更操作类型枚举
 * @author Administrator
 *
 */
public enum ReserveOperateTypeEnums {
	MAKE_APPOINTMENT_1("1", "预约看房"),
    CHANGE_APPOINTMENT_2("2", "变更看房信息"),
    CANCEL_APPOINTMENT_3("3", "取消行程");
	private String code;
	private String message;
	private ReserveOperateTypeEnums(String code, String message) {
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
		ReserveOperateTypeEnums[] reserveStatus = ReserveOperateTypeEnums.values();
		for (ReserveOperateTypeEnums status : reserveStatus) {
			if(StringUtils.equals(status.getCode(),code)){
				return status.getMessage();
			}
		}
		return "";
	}
}