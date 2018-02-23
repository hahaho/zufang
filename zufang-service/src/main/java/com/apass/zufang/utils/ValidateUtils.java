package com.apass.zufang.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.apass.gfb.framework.exception.BusinessException;
import com.apass.gfb.framework.utils.RegExpUtils;
import com.apass.zufang.common.code.BusinessErrorCode;

/**
 * 此工具类的目的，在于减少页面的验证的重复代码
 */
public class ValidateUtils {
	
	private static final Logger logger  = LoggerFactory.getLogger(ValidateUtils.class);
	/**
	 * 验证字符串非空
	 * @param value
	 * @param message
	 * @return
	 * @throws BusinessException 
	 */
	public static void isNotBlank(String value,String message) throws BusinessException{
		if(StringUtils.isBlank(value)){
			logger.error("isNotBlank value:"+value);
			throw new BusinessException(message);
		}
	}
	
	/**
	 * 验证字符串非空
	 * @param value
	 * @param message
	 * @return
	 * @throws BusinessException 
	 */
	public static void isNotBlank(String value,String message,BusinessErrorCode code) throws BusinessException{
		if(StringUtils.isBlank(value)){
			logger.error("isNotBlank error value:"+value);
			throw new BusinessException(message,code.getCode().toString());
		}
	}
	
	/**
	 * 验证传入对象是否为空
	 * @param obj
	 * @param message
	 * @throws BusinessException
	 */
	public static void isNullObject(Object obj,String message) throws BusinessException{
		if(null == obj){
			logger.info("isNullObject:"+JSON.toJSONString(obj));
			throw new BusinessException(message);
		}
	}
	
	/**
	 * 验证传入对象是否为空
	 * @param obj
	 * @param message
	 * @throws BusinessException
	 */
	public static void isNullObject(Object obj,String message,BusinessErrorCode code) throws BusinessException{
		if(null == obj){
			logger.error("isNullObject error obj:"+JSON.toJSONString(obj));
			throw new BusinessException(message,code.getCode().toString());
		}
	}
	/**
	 * 验证字符串的长度，是否在某一限定的区间
	 * @param value
	 * @param min
	 * @param max
	 * @param message
	 * @throws BusinessException 
	 */
	public static void checkLength(String value,int min,int max,String message,BusinessErrorCode code) throws BusinessException{
		if((StringUtils.length(value) < min) || (StringUtils.length(value) > max)){
			logger.error("checkLength value:"+value);
			throw new BusinessException(message,code.getCode().toString());
		}
	}
	
	/**
	 * 验证字符串的长度，是否在某一限定的区间
	 * @param value
	 * @param min
	 * @param max
	 * @param message
	 * @throws BusinessException 
	 */
	public static void checkLength(String value,int min,int max,String message) throws BusinessException{
		if((StringUtils.length(value) < min) || (StringUtils.length(value) > max)){
			logger.error("checkLength error value :"+value);
			throw new BusinessException(message);
		}
	}
	
	/**
	 * 如果传入参数不能为空，则验证长度
	 * @param value
	 * @param min
	 * @param max
	 * @param message
	 * @throws BusinessException
	 */
	public static void checkValueLength(String value,int min,int max,String message) throws BusinessException{
		if(StringUtils.isNotBlank(value)){
			checkLength(value, min, max, message);
		}
	}
	/**
	 * 验证手机号码格式
	 * @param value
	 * @param message
	 * @throws BusinessException
	 */
	public static void checkPhone(String value,String message) throws BusinessException{
		if(StringUtils.length(value) != 11){
			logger.error("mobile length:"+value);
			throw new BusinessException(message);
		}
		if(!RegExpUtils.mobiles(value)){
			logger.error("mobile format:"+value);
			throw new BusinessException(message);
		}
	}
	
	/**
	 * 验证是否为整数
	 * @param value
	 * @param message
	 * @throws BusinessException
	 */
	public static void isNumber(String value,String message) throws BusinessException{
		if(!isNumber(value)){
			throw new BusinessException(message+"值应为数字");
		}
	}
	
	/**
	 * 判断整数数字在某一区间
	 * @param value
	 * @param min
	 * @param max
	 * @param message
	 * @throws BusinessException 
	 */
	public static void checkNumberRange(String value,long min,long max,String message) throws BusinessException{
		isNumber(value,message);
		long val = Long.parseLong(value);
		if(max == 0){
			max = Long.MAX_VALUE;
		}
		if(val < min || val > max) {
			throw new BusinessException(message+"值应该在"+min+"和"+max+"之间");
		}
	}
	
	/**
	 * 判断非整数数字在某一区间
	 * @param value
	 * @param min
	 * @param max
	 * @param message
	 * @throws BusinessException
	 */
	public static void checkNonNumberRange(String value,long min,long max,String message) throws BusinessException{
		isNonNumber(value);
		long val = Long.parseLong(value);
		if(max == 0){
			max = Long.MAX_VALUE;
		}
		if(val < min || val > max) {
			throw new BusinessException(message+"值应该在"+min+"和"+max+"之间");
		}
	}
	
	/**
	 * 验证是否为整数
	 * @param value
	 * @param message
	 * @throws BusinessException
	 */
	public static void isNonNumber(String value,String message) throws BusinessException{
		if(!isNonNumber(value)){
			throw new BusinessException(message+"值应为数字");
		}
	}
	/**
	 * 验证整数
	 * @param value
	 * @return
	 */
	public static boolean isNumber(String value){
		Pattern pattern = Pattern.compile("^-?[0-9]+");
	    Matcher isNum = pattern.matcher(value);
	    if(!isNum.matches() ){
	       return false; 
	    } 
		return true;
	}
	
	/**
	 * 验证非整数
	 * @param value
	 * @return
	 */
	public static boolean isNonNumber(String value){
		Pattern pattern = Pattern.compile("^[-+]?[0-9]+(\\.[0-9]+)?$");
		Matcher isNum = pattern.matcher(value); // matcher是全匹配
        if (!isNum.matches()) {
            return false;
        }
		return true;
	}
}
