package com.gmfiot.data;

import com.gmfiot.core.data.Query;
import com.gmfiot.core.model.BaseModel;
import com.gmfiot.core.util.Inflector;
import com.gmfiot.core.util.ReflectionUtil;
import com.gmfiot.core.util.StringUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class SqlServerSqlGenerator implements SqlGenerator {
    private final static ConcurrentMap<String,TableInfo> TABLE_MAP;
    private final static ConcurrentMap<String,QueryInfo> QUERY_MAP;

    static {
        TABLE_MAP = new ConcurrentHashMap<>();
        QUERY_MAP = new ConcurrentHashMap<>();
    }

    /**
     * 根据数据模型类型获取表信息
     * @param clazz
     * @return
     */
    public static TableInfo getTableInfo(Class<?> clazz){
        var tableInfo = TABLE_MAP.get(clazz.getName());
        if(tableInfo == null){
            var typeName = clazz.getName();
            var tableName = Inflector.getInstance().pluralize(clazz.getSimpleName());
            var fieldList = ReflectionUtil.getAllFields(clazz);
            tableInfo = new TableInfo();
            tableInfo.setTableName(tableName);;
            tableInfo.setColumns(fieldList);
            var getMethodMap = new HashMap<String, Method>();
            fieldList.stream().forEach(fieldName -> {
                try {
                    var getMethod = clazz.getMethod("get" + StringUtil.toCapName(fieldName));
                    getMethodMap.put(fieldName,getMethod);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            });
            tableInfo.setGetMethodMap(getMethodMap);
            TABLE_MAP.putIfAbsent(typeName,tableInfo);
        }
        return tableInfo;
    }

    /**
     * 获取非空字段名
     * @param object
     * @return
     */
    public static List<String> getNotNullColumns(Object object) {
        List<String> notNullColumnList = new ArrayList<>();
        var clazz = object.getClass();
        var tableInfo = TABLE_MAP.get(clazz.getName());
        try {
            for (var columnName : tableInfo.getColumns())
            {
                var getMethod = tableInfo.getGetMethodMap().get(columnName);
                var retValue = getMethod.invoke(object);
                if(retValue != null){
                    notNullColumnList.add(columnName);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return notNullColumnList;
    }

    public static QueryInfo getQueryInfo(Class<?> clazz){
        var queryInfo = QUERY_MAP.get(clazz.getName());
        if(queryInfo == null){
            var typeName = clazz.getName();
            var fieldList = ReflectionUtil.getAllFields(clazz);
            queryInfo = new QueryInfo();
            queryInfo.setTypeName(typeName);
            queryInfo.setProperties(fieldList);
            var getMethodMap = new HashMap<String, Method>();
            fieldList.stream().forEach(fieldName -> {
                try {
                    var getMethod = clazz.getMethod("get" + StringUtil.toCapName(fieldName));
                    getMethodMap.put(fieldName,getMethod);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            });
            queryInfo.setGetMethodMap(getMethodMap);
            QUERY_MAP.putIfAbsent(typeName,queryInfo);
        }
        return queryInfo;
    }

    public static Map<String,Object> getPropertyNameValueMapForQuery(Object object) {
        Map<String,Object> propertyMap = new HashMap<>();
        var clazz = object.getClass();
        var queryInfo = QUERY_MAP.get(clazz.getName());
        try {
            for (var propertyName : queryInfo.getProperties())
            {
                var getMethod = queryInfo.getGetMethodMap().get(propertyName);
                var retValue = getMethod.invoke(object);
                if(retValue != null){
                    propertyMap.putIfAbsent(propertyName,retValue);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return propertyMap;
    }

    public static String buildOrderBySql(Query query, Class<? extends BaseModel> clazz){
        if(query == null){
            return "";
        }
//        var queryNotNullProperties = getNotNullPropertyMap(query);
        StringBuilder stringBuilder = new StringBuilder();
        return  stringBuilder.toString();
    }

    public static String buildWheresSql(Query query, Class<? extends BaseModel> clazz){
        var queryNotNullProperties = getNotNullColumns(query);

        return  "";
    }

}
