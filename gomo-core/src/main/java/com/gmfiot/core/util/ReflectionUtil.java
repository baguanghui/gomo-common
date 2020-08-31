package com.gmfiot.core.util;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;

/**
 * @author ThinkPad
 */
public class ReflectionUtil {
    /**
     * 反射创建对象
     * @param c
     * @param <T>
     * @return
     */
    public static <T> T getObject(Class<T> type){
        T obj = null;
        try {
            obj = type.getConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * 获泛型数组
     * @param type
     * @param n
     * @param <T>
     * @return
     */
    public static <T> T[] getObjectArray(Class<T> type,int n){
        var array = (T[])Array.newInstance(type,n);
        return  array;
    }
}
