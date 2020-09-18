package com.gmfiot.core.util;

/**
 * @author BaGuangHui
 */
public class StringUtil {

    /**
     * 判断字符串是空
     * @param str
     * @return
     */
    public static boolean isEmpty(String str){
        return str == null || str.isEmpty();
    }

    /**
     * 判断字符串是否为空或空格字符
     * @param str
     * @return
     */
    public static boolean isBlank(String str){
        return str == null || str.isBlank();
    }

    /**
     * 字符串首字母转大写
     * @param str
     * @return
     */
    public static String capitalize(String str){
        char[] cs=str.toCharArray();
        cs[0]-=32;
        return String.valueOf(cs);
    }

    /**
     * 字符串首字母转小写
     * @param str
     * @return
     */
    public static String decapitalize(String str){
        char[] cs=str.toCharArray();
        cs[0]+=32;
        return String.valueOf(cs);
    }
}
