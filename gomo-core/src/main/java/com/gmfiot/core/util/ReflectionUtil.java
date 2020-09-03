package com.gmfiot.core.util;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
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
        for (var field : clazz.getSuperclass().getDeclaredFields())
        {
            fieldList.add(field.getName());
        }
        for (var field : clazz.getDeclaredFields())
        {
            fieldList.add(field.getName());
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
                method.setAccessible(true);
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
//        user.setStatus(1);
//        user.setDirectory(2);
//        user.setReferenceId("Refer");
//        user.setFailures(1);
//
//        var fieldList = getNotNullFields(user);
//        System.out.println(fieldList);
        //System.out.println(Inflector.plural("",""););


//        StringBuffer sql = new StringBuffer("insert into users(");
//        var fieldList = ReflectionUtil.getNotNullFields(user);
//        var columnStr = String.join(",",fieldList);
//        sql.append(columnStr);
//        sql.append(") values (");
//        List<String> values = fieldList.stream().map(field -> String.format("#{%s}",field)).collect(Collectors.toList());
//        var valueStr = String.join(",",values);
//        sql.append(valueStr);
//        sql.append(")");
//        System.out.println(sql.toString());
//
//        var testUser = new TestUser(){
//            {
//                setId();
//            }
//        };

       Map<String,String> map = new HashMap<>(){
           {
               put("a","1");
               put("b","2");
           }
       };
       System.out.println( map );
       System.out.printf("Hi,%s,%s,%s","张三","李四","王五");
    }
}

class TestUser{
    public void setId(){

    }
}
