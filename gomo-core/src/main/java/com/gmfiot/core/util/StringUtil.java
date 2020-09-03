package com.gmfiot.core.util;

public class StringUtil {
    public static boolean isEmpty(String str){
        return str == null || str.isEmpty();
    }

    public static boolean isBlank(String str){
        return str == null || str.isBlank();
    }

    /**
     * 字符串首字母转大写
     * @param str
     * @return
     */
    public static String toCapName(String str){
        char[] cs=str.toCharArray();
        cs[0]-=32;
        return String.valueOf(cs);
    }
}
