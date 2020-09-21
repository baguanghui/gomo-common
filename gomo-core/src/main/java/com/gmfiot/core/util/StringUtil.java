package com.gmfiot.core.util;

import com.gmfiot.core.BusinessException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    /**
     * 解析字符串模板，把占位符替换成对应的对象值
     * @param object
     * @param template
     * @return
     */
    public static String getTemplateText(Object object,String template){
        var fieldValueMap = ReflectionUtil.getFieldValueMap(object);
        String regex = "\\{\\w*}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(template);
        while (matcher.find()){
            String group = matcher.group();
            String key = group.substring(1, group.length() - 1);
            if (!fieldValueMap.containsKey(key)) {
                throw new BusinessException("not found key：" + key);
            }
            if (fieldValueMap.get(key) == null) {
                throw new BusinessException( key + " cannot be null");
            }
            template = template.replace(group, fieldValueMap.get(key).toString());
        }
        return template;
    }
}
