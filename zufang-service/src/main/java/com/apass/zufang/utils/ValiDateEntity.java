package com.apass.zufang.utils;
import java.lang.reflect.Field;
import com.apass.gfb.framework.exception.BusinessException;
public class ValiDateEntity {
	/**
	 * 构造方法私有
	 */
    private ValiDateEntity() {}
    public static void valiDateEntity(Object o, Class< ? > c) throws BusinessException {
    	Field[ ] fields = c.getDeclaredFields( );  
        for ( Field field : fields ) {
        	field.setAccessible( true );
        	String key = field.getName();
        	Object value = null;
        	try {
				value = field.get(o);
				if(value==null){
	    			throw new BusinessException("参数" + key + "为空！");
	    		}else{
	    			ValidateUtils.isNotBlank(value.toString(), "参数" + key + "为空！");
	    		}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				continue;
			}
        }
    }
}
