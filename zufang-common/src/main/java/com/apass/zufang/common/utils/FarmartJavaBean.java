package com.apass.zufang.common.utils;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
/**
 * FarmartJavaBean
 * 持久化类和键值对互相转化
 * @author Administrator
 *
 */
public class FarmartJavaBean{
	/**
	 * 构造方法私有
	 */
    private FarmartJavaBean() {}
    /**
     * map2entity
     * @param o
     * @param c
     * @param value
     * @param name
     * @return
     */
    public static Object map2entity(Object o, Class< ? > c ,Map<String,Object> map){
        Set<Entry<String, Object>> set = map.entrySet();
        for(Entry<String, Object> s : set){
            Object value = s.getValue();
            String name = s.getKey();
            o = farmartJavaB(o, c, value, name);
        }
        return o;
    }
    /**
     * entity2map
     * @param o
     * @param c
     * @return
     * @throws IllegalAccessException 
     * @throws IllegalArgumentException 
     */
    public static Map<String,Object> entity2map(Object o, Class< ? > c) {
    	Map<String,Object> map = new HashMap<String,Object>();
		Field[ ] fields = c.getDeclaredFields( );  
        for ( Field field : fields ) {  
        	field.setAccessible( true );
        	String key = field.getName();
        	Object value = null;
			try {
				value = field.get(o);
				map.put(key, value);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				continue;
			}
        }
        return map;
    }
    /**
     * 格式化字符串   驼峰命名
     * @param fieldname
     * @return
     */
    public static String farmartField(String fieldname){
        StringBuilder result = new StringBuilder();  
        if (fieldname == null || fieldname.isEmpty()) {  
            return "";  
        } else if (!fieldname.contains("_")) {  
            // 不含下划线
            return fieldname.toLowerCase();  
        }  
        // 用下划线将原始字符串分割  
        String camels[] = fieldname.split("_");  
        for (String camel :  camels) {  
            // 跳过原始字符串中开头、结尾的下换线或双重下划线  
            if (camel.isEmpty()) {  
                continue;  
            }  
            // 处理真正的驼峰片段  
            if (result.length() == 0) {  
                // 第一个驼峰片段，全部字母都小写  
                result.append(camel.toLowerCase());  
            } else {  
                // 其他的驼峰片段，首字母大写  
                result.append(camel.substring(0, 1).toUpperCase());  
                result.append(camel.substring(1).toLowerCase());  
            }  
        }  
        return result.toString(); 
    }
    /**
     * 格式化实体类null字段为空字段
     * @param o
     * @param c
     * @return
     */
    public static Object farmartJavaB(Object o, Class< ? > c ){
        // 获取父类，判断是否为相同对象  
        // 获取类中的所有定义字段  
        Field[ ] fields = c.getDeclaredFields( );  
        // 循环遍历字段，获取字段对应的属性值  
        for ( Field field : fields ) {  
            // 如果不为空，设置可见性，然后返回  
            field.setAccessible( true );  
            try {  
                // 设置字段可见，即可用get方法获取属性值。  
                if(field.getType().equals(String.class)){
                    if(field.get( o )==null){
                        field.set(o, "");
                    }
                }
            }catch ( Exception e ) {  
            }  
        }  
        return o;
    }
    /**
     * 填充实体类
     * @param o
     * @param c
     * @param value
     * @param name
     * @return
     */
    public static Object farmartJavaB(Object o, Class< ? > c , Object value , String name){
        // 获取父类，判断是否为相同对象   判断字段值有效性
    	if(!o.getClass().equals(c)||!(value!=null&&value!="")){
    		return o;
    	}
        List<Field> fields = new ArrayList<Field>() ;
        //当父类为null的时候说明到达了最上层的父类(Object类).  剔除Object
        while (c !=null && !c.getName().toLowerCase().equals("java.lang.object")) {
        	fields.addAll(Arrays.asList(c .getDeclaredFields()));
        	//得到父类,然后赋给自己
        	c = c.getSuperclass(); 
        }
        // 循环遍历字段，获取字段对应的属性值  
        for ( Field field : fields ) {  
        	// 设置字段可见，即可调用get、set方法  
            field.setAccessible( true );  
            // 如果不为空，设置可见性，然后返回  
            try {  
                switch(field.getGenericType().toString()){
                    case "class java.lang.String":
                        if(field.getName().equals(name)){
                            field.set(o, value);
                        }
                        break;
                    case "class java.lang.Integer":
                        if(field.getName().equals(name)){
                            field.setInt(o, Integer.parseInt(value.toString()));
                        }
                        break;
                    case "long":
                        if(field.getName().equals(name)){
                            field.setLong(o, (long) value);
                        }
                        break;
                    case "class java.math.BigDecimal":
                        if(field.getName().equals(name)){
                            field.set(o, new BigDecimal(value.toString()));
                        }
                        break;
                    default:
                        break;                           
                } 
            }catch ( Exception e ) {  
            }  
        }  
        return o;
    }
}