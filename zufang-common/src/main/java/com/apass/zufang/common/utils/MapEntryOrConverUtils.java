package com.apass.zufang.common.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class MapEntryOrConverUtils {
	
	/**
	 * 把map装换成javaBean
	 * @param type javaBean 类
	 * @param map 转化的map
	 * @return
	 * @throws IntrospectionException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 */
	public static Object converMap(Class type,Map map) throws IntrospectionException, InstantiationException, IllegalAccessException, IllegalArgumentException{
		BeanInfo beanInfo = Introspector.getBeanInfo(type); // 获取类属性  
		Object obj = type.newInstance(); // 创建 JavaBean 对象    
		PropertyDescriptor[] propertyDescriptors =  beanInfo.getPropertyDescriptors();    
        for (PropertyDescriptor descriptor: propertyDescriptors) {    
            String propertyName = descriptor.getName();
            if (map.containsKey(propertyName)) {    
            	try {
            		Object value = map.get(propertyName);    
                    descriptor.getWriteMethod().invoke(obj, value);
				} catch (Exception e) {}
            }    
        }    
        return obj;
	}
	
	/**
	 * 实体类转成map
	 * @param bean
	 * @return
	 * @throws IntrospectionException
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 */
	public static Map converObject(Object bean) throws IntrospectionException, IllegalAccessException, IllegalArgumentException{
		Map map = new HashMap();
		Class type = bean.getClass();
		BeanInfo beanInfo = Introspector.getBeanInfo(type); // 获取类属性  
		PropertyDescriptor[] propertyDescriptors =  beanInfo.getPropertyDescriptors(); 
		for (PropertyDescriptor descriptor: propertyDescriptors) {
			String propertyName = descriptor.getName();
			if(!StringUtils.equals(propertyName, "class")){
				Method readMethod = descriptor.getReadMethod();
				Object result = null;
				try {
					result = readMethod.invoke(bean);
				} catch (Exception e) {}
				if(result == null){
					result = "";
				}
				map.put(propertyName, result);
			}
		}
		return map;
	}
}
