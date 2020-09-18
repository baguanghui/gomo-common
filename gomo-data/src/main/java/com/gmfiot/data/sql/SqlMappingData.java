package com.gmfiot.data.sql;

import com.gmfiot.core.util.Inflector;
import com.gmfiot.core.util.ReflectionUtil;
import com.gmfiot.core.util.StringUtil;
import com.gmfiot.data.annotation.Column;
import com.gmfiot.data.annotation.Table;
import org.springframework.lang.Nullable;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author BaGuangHui
 */
public final class SqlMappingData {

    private final static ConcurrentMap<String, TableInfo> TABLE_MAP;
    private final static ConcurrentMap<String, QueryInfo> QUERY_MAP;
    /**
     * sql映射数据类型
     */
    private static List<String> sqlMapJavaDataType = List.of("java.lang.String","java.math.BigDecimal","java.lang.Boolean","java.lang.Byte","java.lang.Short","java.lang.Integer","java.lang.Long","java.lang.Float","java.lang.Double","byte[]","java.util.Date","java.sql.Date","java.sql.Time","java.sql.Timestamp","java.lang.Object");

    static {
        TABLE_MAP = new ConcurrentHashMap<>();
        QUERY_MAP = new ConcurrentHashMap<>();
    }

    /**
     * 根据数据模型类型获取表信息
     * @param modelClass
     * @return
     */
    public static TableInfo getTableInfo(Class<?> modelClass){
        var tableInfo = TABLE_MAP.get(modelClass.getName());
        if(tableInfo == null){
            var typeName = modelClass.getName();
            tableInfo = new TableInfo();
            TABLE_MAP.putIfAbsent(typeName,tableInfo);
            var tableAnnotation = modelClass.getAnnotation(Table.class);
            if(tableAnnotation != null){
                tableInfo.setName(tableAnnotation.name());
                tableInfo.setSchema(tableAnnotation.schema());
            }else {
                var tableName = Inflector.getInstance().pluralize(modelClass.getSimpleName());
                tableInfo.setName(tableName);
            }
            var fieldList = ReflectionUtil.getAllFields(modelClass);
            var fieldColumnMap = new HashMap<String,ColumnInfo>();
            for (var fieldName : fieldList){
                try {
                    var columnInfo = new ColumnInfo();
                    var field = ReflectionUtil.getField(modelClass,fieldName);
                    var fieldTypeName =field.getGenericType().getTypeName();
                    if(!sqlMapJavaDataType.contains(fieldTypeName)){
                        continue;
                    }
                    columnInfo.setTypeName(fieldTypeName);
                    var fieldAnnotation = field.getAnnotation(Column.class);
                    if(fieldAnnotation == null){
                        columnInfo.setName(fieldName);
                    }else {
                        columnInfo.setName(fieldAnnotation.name());
                        columnInfo.setNullable(fieldAnnotation.nullable());
                    }
                    var readMethod = modelClass.getMethod("get" + StringUtil.capitalize(fieldName));
                    columnInfo.setReadMethod(readMethod);

                    var writeMethod = modelClass.getMethod("set" + StringUtil.capitalize(fieldName),field.getType());
                    columnInfo.setWriteMethod(writeMethod);

                    fieldColumnMap.putIfAbsent(fieldName,columnInfo);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
            tableInfo.setFieldColumnMap(fieldColumnMap);
        }
        return tableInfo;
    }

    /**
     * 获取非空字段名
     * @param model
     * @return
     */
    public static List<String> getNotNullFields(Object model) {
        List<String> notNullFieldList = new ArrayList<>();
        var modelClass = model.getClass();
        var tableInfo = TABLE_MAP.get(modelClass.getName());
        try {
            var entrySet = tableInfo.getFieldColumnMap().entrySet();
            for(var entry : entrySet){
                var retValue = entry.getValue().getValue(model);
                if(retValue != null){
                    notNullFieldList.add(entry.getKey());
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return notNullFieldList;
    }

    public static QueryInfo getQueryInfo(Class<?> queryClass){
        var queryInfo = QUERY_MAP.get(queryClass.getName());
        if(queryInfo == null){
            var typeName = queryClass.getName();
            var fieldList = ReflectionUtil.getAllFields(queryClass);
            queryInfo = new QueryInfo();
            queryInfo.setTypeName(typeName);
            queryInfo.setFields(fieldList);

            var readMethodMap = new HashMap<String, Method>();
            var conditionNameMap = new HashMap<String,String>();

            for (var fieldName : fieldList){
                try {
                    var conditionName = "";
                    if(fieldName.startsWith("or")){
                        conditionName = StringUtil.decapitalize(fieldName.replace("or",""));
                    } else if(fieldName.startsWith("and")){
                        conditionName = StringUtil.decapitalize(fieldName.replace("and",""));
                    }
                    conditionNameMap.putIfAbsent(fieldName,conditionName);

                    var getMethod = queryClass.getMethod("get" + StringUtil.capitalize(fieldName));
                    readMethodMap.put(fieldName,getMethod);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
            queryInfo.setReadMethodMap(readMethodMap);
            queryInfo.setConditionNameMap(conditionNameMap);
            QUERY_MAP.putIfAbsent(typeName,queryInfo);
        }
        return queryInfo;
    }

    public static Map<String,Object> getNotNullQueryFieldMap(Object object) {
        Map<String,Object> propertyMap = new HashMap<>();
        var queryInfo = getQueryInfo(object.getClass());
        try {
            for (var fieldName : queryInfo.getFields())
            {
                var getMethod = queryInfo.getReadMethodMap().get(fieldName);
                var retValue = getMethod.invoke(object);
                if(retValue != null){
                    propertyMap.putIfAbsent(fieldName,retValue);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return propertyMap;
    }

    public static Object getColumnValue(ResultSet resultSet, int columnindex, ColumnInfo columnInfo) {

        try {
            switch (columnInfo.getTypeName()){
                case "java.lang.String":
                    return resultSet.getString(columnindex);
                case "java.math.BigDecimal":
                    return resultSet.getBigDecimal(columnindex);
                case "java.lang.Boolean":
                    return resultSet.getBoolean(columnindex);
                case "java.lang.Byte":
                    return resultSet.getByte(columnindex);
                case "java.lang.Short":
                    return resultSet.getShort(columnindex);
                case "java.lang.Integer":
                    return resultSet.getInt(columnindex);
                case "java.lang.Long":
                    return resultSet.getLong(columnindex);
                case "java.lang.Float":
                    return resultSet.getFloat(columnindex);
                case "java.lang.Double":
                    return resultSet.getDouble(columnindex);
                case "byte[]":
                    return resultSet.getBytes(columnindex);
                case "java.util.Date":
                    return resultSet.getDate(columnindex);
                case "java.sql.Date":
                    return resultSet.getDate(columnindex);
                case "java.sql.Time":
                    return resultSet.getTime(columnindex);
                case "java.sql.Timestamp":
                    return resultSet.getTimestamp(columnindex);
                case "java.lang.Object":
                    return resultSet.getObject(columnindex);
                default:
                    break;
            }
        } catch (Exception exception){
            exception.printStackTrace();
        }
        return null;
    }

}
