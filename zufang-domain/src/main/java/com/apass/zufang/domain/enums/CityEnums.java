package com.apass.zufang.domain.enums;

import org.apache.commons.codec.binary.StringUtils;

public enum CityEnums {

	city_1("1", "北京"),
    city_2("2", "上海"),
    city_3("3", "天津"),
    city_4("4", "重庆");
    
    private String code;
	
	private String message;
	
	private CityEnums(String code, String message) {
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
	
	public static boolean isContains(String name) {
        CityEnums[] cityArray = CityEnums.values();
        for (CityEnums cityEnum : cityArray) {
        	if (StringUtils.equals(cityEnum.getMessage(), name)) {
                return true;
            }
        }
        return false;
    }
}
