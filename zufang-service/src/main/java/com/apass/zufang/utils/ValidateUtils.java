package com.apass.esp.utils;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.apass.esp.common.code.BusinessErrorCode;
import com.apass.gfb.framework.exception.BusinessException;
import com.apass.gfb.framework.utils.RegExpUtils;

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
			throw new BusinessException(message,code);
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
			throw new BusinessException(message,code);
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
			throw new BusinessException(message,code);
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
}
