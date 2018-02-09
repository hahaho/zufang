package com.apass.zufang.utils;

import org.apache.commons.lang3.StringUtils;

public class ToolsUtils {

	/**
	 * 获取指定位置的字符串
	 * @param value 传入字符串
	 * @param num 截取后几位
	 * @return
	 */
	public static String getLastStr(String value,int num){
		if(StringUtils.isNotBlank(value) && StringUtils.length(value) > num){
			return value.substring(value.length() - num,value.length());
		}
		return "";
	}
	
	/**
	 * 随机生成5位数字
	 * @return
	 */
	public static int fiveRandom(){
		return (int)((Math.random()*9+1)*10000);
	}
	
}
