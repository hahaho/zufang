package com.apass.zufang.domain.enums;
/**
 * 特色配置
 * @author zwd
 *
 */
public enum FeaturesConfigurationEnums {
	
    FEATURES_1(1, "电视"),
    FEATURES_2(2, "空调"),
    FEATURES_3(3, "热水器"),
    FEATURES_4(4, "洗衣机"),
    FEATURES_5(5, "冰箱"),
    FEATURES_6(6, "床"),
    FEATURES_7(7, "沙发"),
    FEATURES_8(8, "衣柜"),
    FEATURES_9(9, "暖气"),
    FEATURES_10(10, "宽带网"),
    FEATURES_11(11, "可做饭"),
    FEATURES_12(12, "独立阳台"),
    FEATURES_13(13, "独卫");
	
	private Integer code;

	private String message;
	
	private FeaturesConfigurationEnums(Integer code, String message) {
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
