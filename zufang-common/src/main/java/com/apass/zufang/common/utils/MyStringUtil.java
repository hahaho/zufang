package com.apass.zufang.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xiaohai on 2018/1/5.
 */
public class MyStringUtil {
    /**
     * 判断字符串是否是数字的方法
     */
    //使用 Java自带的函数
    public static boolean isNumeric1 (String str) {
        for (int i = str.length(); --i >=0;) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 使用正则表达式 isNumeric2-isNumeric4
     * @param str
     * @return
     */
    public static boolean isNumeric2(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    public static boolean isNumeric3(String str) {
        if (str != null && !"".equals(str.trim())) {
            return str.matches("^[0-9]*$");
        }
        return false;
    }
    public static boolean isNumeric4 (String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }


    /**
     * 使用 ASCII 码
     * @param str
     * @return
     */
    public static boolean isNumeric5 (String str) {
        for (int i = str.length(); --i>=0;) {
            int chr = str.charAt(i);
            if (chr < 48 || chr > 57) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是不是浮点型数据
     * @param str
     * @return
     */
    public static boolean isDouble(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");
        return pattern.matcher(str).matches();
    }


    /**
     * 提取字符串中的数字
     * @param str
     */
    public static String  getNumFromStr(String str) {
        //匹配非数字内容
        String regEx="[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);

        //把所有非数字数据删除，只留数字并去除首尾空格
        return m.replaceAll("").trim();
    }


}
