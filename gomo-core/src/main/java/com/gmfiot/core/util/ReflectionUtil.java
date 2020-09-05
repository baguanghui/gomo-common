package com.gmfiot.core.util;

import com.gmfiot.core.data.Result;

import java.lang.reflect.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ThinkPad
 */
public class ReflectionUtil {
    /**
     * 反射创建对象
     * @param
     * @param <T>
     * @return
     */
    public static <T> T getObject(Class<T> tClass){
        T obj = null;
        try {
            obj = tClass.getConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * 获泛型数组
     * @param tClass
     * @param n
     * @param <T>
     * @return
     */
    public static <T> T[] getObjectArray(Class<T> tClass, int n){
        var array = (T[])Array.newInstance(tClass,n);
        return  array;
    }

    /**
     * 获取类型所有字段名
     * @param clazz
     * @return
     */
    public static List<String> getAllFields(Class<?> clazz){
        List<String> fieldList = new ArrayList<>();
        while (clazz != null){
            for (var field : clazz.getDeclaredFields())
            {
                fieldList.add(field.getName());
            }
            if(clazz.getSuperclass() == Object.class)
            {
                break;
            }
            clazz =  clazz.getSuperclass();
        }
        return fieldList;
    }

    /**
     * 获取所有非空字段名
     * @param object
     * @return
     */
    public static List<String> getNotNullFields(Object object) {
        List<String> notNullfieldList = new ArrayList<>();
        var clazz = object.getClass();
        var fieldList = getAllFields(clazz);
        try {
            for(var fieldName : fieldList)
            {
                var method = clazz.getMethod("get" + StringUtil.toCapName(fieldName));
                //method.setAccessible(true);
                var retValue = method.invoke(object);
                if(retValue != null){
                    notNullfieldList.add(fieldName);
                }
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e){
            e.printStackTrace();
        } catch (IllegalAccessException e){
            e.printStackTrace();
        }
        return notNullfieldList;
    }

    /**
     * 通过class类型获取获取对应类型的值
     * @param typeClass
     * @param value
     * @return
     */
    public static Object getClassTypeValue(Class<?> typeClass,Object value){
        if(typeClass == int.class || value instanceof  Integer){
            if(null == value){
                return 0;
            }
            return value;
        } else if(typeClass == short.class){
            if(null == value){
                return 0;
            }
            return value;
        }else if(typeClass == byte.class){
            if(null == value){
                return 0;
            }
            return value;
        }else if(typeClass == double.class){
            if(null == value){
                return 0;
            }
            return value;
        }else if(typeClass == long.class){
            if(null == value){
                return 0;
            }
            return value;
        }else if(typeClass == String.class){
            if(null == value){
                return "";
            }
            return value;
        }else if(typeClass == boolean.class){
            if(null == value){
                return true;
            }
            return value;
        }else if(typeClass == BigDecimal.class){
            if(null == value){
                return new BigDecimal(0);
            }
            return new BigDecimal(value+"");
        }
        else {
            return typeClass.cast(value);
        }
    }


    /**
     * 获取到当前方法名称
     * @return
     */
    public static String getCurrentMethodName(){
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        return  methodName;
    }


    public static void main(String[] args) throws InterruptedException {
        var user = new User();
        user.setName("张三");
        user.setId(9000011L);
        user.setCreatedAt(new Date());
    }
}