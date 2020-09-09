package com.gmfiot.data.sql;

import com.gmfiot.core.util.Inflector;
import com.gmfiot.core.util.ReflectionUtil;
import com.gmfiot.core.util.StringUtil;
import com.gmfiot.data.annotation.Column;
import com.gmfiot.data.annotation.Table;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

/**
 * for mybatis
 */
public class SqlServerSqlBuilder implements SqlBuilder {

    private final static ConcurrentMap<String, TableInfo> TABLE_MAP;
    private final static ConcurrentMap<String, QueryInfo> QUERY_MAP;

    private StringBuilder sqlStringBuilder;
    private Class modelClass;
    private Object model;
    private Object query;

    static {
        TABLE_MAP = new ConcurrentHashMap<>();
        QUERY_MAP = new ConcurrentHashMap<>();
    }

    public SqlServerSqlBuilder(){}

    public SqlServerSqlBuilder(Class mClass, Object model, Object query){
        this.sqlStringBuilder = new StringBuilder();
        this.modelClass = mClass;
        this.model = model;
        this.query = query;
    }

    @Override
    public SqlBuilder build(SqlTypeEnum sqlTypeEnum) {

        switch (sqlTypeEnum){
            case INSERT:
                buildInsert();
                break;
            case DELETE:
                buildDelete();
                break;
            case UPDATE:
                buildUpdate();
                break;
            case SELECT:
                buildSelect();
                break;
            default:
                throw new UnsupportedOperationException("Unsupported SqlTypeEnum");
        }
        return this;
    }

    @Override
    public SqlBuilder build(SqlClauseTypeEnum sqlClauseTypeEnum) {
        switch (sqlClauseTypeEnum){
            case WHERE:
                buildWhere();
                break;
            case ORDERBY:
                buildOrderBy();
                break;
            case TOP:
                buildTop();
                break;
            case OFFSET_FETCH:
                buildOffsetFetch();
                break;
            case GROUPBY:
                buildGroupBy();
                break;
            case HAVING:
                buildHaving();
                break;
            default:
                throw new UnsupportedOperationException("Unsupported SqlClauseTypeEnum");
        }
        return this;
    }

    public static <M, Q> SqlBuilder getBuilder(M model, Q query) {
        var sqlBuilder = new SqlServerSqlBuilder(model.getClass(),model,query);
        return sqlBuilder;
    }

    public static <M, Q> SqlBuilder getBuilder(Class<M> mClass, Q query) {
        var sqlBuilder = new SqlServerSqlBuilder(mClass,null,query);
        return sqlBuilder;
    }

    public static <M, Q> SqlBuilder getBuilder(M model) {
        var sqlBuilder = new SqlServerSqlBuilder(model.getClass(),model,null);
        return sqlBuilder;
    }

    public static <M, Q> SqlBuilder getBuilder(Class<M> mClass) {
        var sqlBuilder = new SqlServerSqlBuilder(mClass,null,null);
        return sqlBuilder;
    }

    @Override
    public String toString() {
        return sqlStringBuilder.toString();
    }

    /**
     * 根据数据模型类型获取表信息
     * @param modelClass
     * @return
     */
    private TableInfo getTableInfo(Class<?> modelClass){
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
            //tableInfo.setColumns(fieldList);
            var fieldColumnMap = new HashMap<String,ColumnInfo>();
            //var getMethodMap = new HashMap<String, Method>();
            fieldList.stream().forEach(fieldName -> {
                try {
                    var columnInfo = new ColumnInfo();
                    var field = ReflectionUtil.getField(modelClass,fieldName);
                    var fieldAnnotation = field.getAnnotation(Column.class);
                    if(fieldAnnotation == null){
                        columnInfo.setName(fieldName);
                    }else {
                        columnInfo.setName(fieldAnnotation.name());
                        columnInfo.setNullable(fieldAnnotation.nullable());
                    }
                    var getMethod = modelClass.getMethod("get" + StringUtil.toUpperCaseFirstLetter(fieldName));
                    columnInfo.setGetMethod(getMethod);
                    fieldColumnMap.putIfAbsent(fieldName,columnInfo);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            });
            tableInfo.setFieldColumnMap(fieldColumnMap);
        }
        return tableInfo;
    }

    /**
     * 获取非空字段名
     * @param model
     * @return
     */
    private List<String> getNotNullFields(Object model) {
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
//            for (var fieldName : tableInfo.getFieldColumnMap().keySet())
//            {
//                var getMethod = tableInfo.getGetMethodMap().get(fieldName);
//                var retValue = getMethod.invoke(model);
//                if(retValue != null){
//                    notNullFieldList.add(fieldName);
//                }
//            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return notNullFieldList;
    }

//    private List<String> getNotNullColumns(Object model) {
//        List<String> notNullColumnList = new ArrayList<>();
//        var modelClass = model.getClass();
//        var tableInfo = TABLE_MAP.get(modelClass.getName());
//        try {
//            var entrySet = tableInfo.getFieldColumnMap().entrySet();
//            for(var entry : entrySet){
//                var retValue = entry.getValue().getValue(model);
//                if(retValue != null){
//                    notNullColumnList.add(entry.getValue().getName());
//                }
//            }
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//        return notNullColumnList;
//    }

    private QueryInfo getQueryInfo(Class<?> queryClass){
        var queryInfo = QUERY_MAP.get(queryClass.getName());
        if(queryInfo == null){
            var typeName = queryClass.getName();
            var fieldList = ReflectionUtil.getAllFields(queryClass);
            queryInfo = new QueryInfo();
            queryInfo.setTypeName(typeName);
            queryInfo.setFields(fieldList);
            var getMethodMap = new HashMap<String, Method>();
            var conditionNameMap = new HashMap<String,String>();

            fieldList.stream().forEach(fieldName -> {
                try {
                    var conditionName = "";
                    if(fieldName.startsWith("or")){
                        conditionName = StringUtil.toLowerCaseFirstLetter(fieldName.replace("or",""));
                    } else if(fieldName.startsWith("and")){
                        conditionName = StringUtil.toLowerCaseFirstLetter(fieldName.replace("and",""));
                    }
                    conditionNameMap.putIfAbsent(fieldName,conditionName);

                    var getMethod = queryClass.getMethod("get" + StringUtil.toUpperCaseFirstLetter(fieldName));
                    getMethodMap.put(fieldName,getMethod);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            });
            queryInfo.setGetMethodMap(getMethodMap);
            queryInfo.setConditionNameMap(conditionNameMap);
            QUERY_MAP.putIfAbsent(typeName,queryInfo);
        }
        return queryInfo;
    }

    public Map<String,Object> getNotNullQueryFieldMap(Object object) {
        Map<String,Object> propertyMap = new HashMap<>();
        var queryInfo = getQueryInfo(object.getClass());
        try {
            for (var fieldName : queryInfo.getFields())
            {
                var getMethod = queryInfo.getGetMethodMap().get(fieldName);
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

    /**************************构建Sql语句******************************/
    /**
     * 构建insert 语句
     */
    private void buildInsert(){
        var tableInfo = getTableInfo(modelClass);
        sqlStringBuilder.append(String.format("INSERT INTO %s(",tableInfo.getName()));
        var fieldList = getNotNullFields(model);
        var columnList = fieldList.stream().map(field -> tableInfo.getFieldColumnMap().get(field).getName()).collect(toList());
        var columnStr = String.join(",",columnList);
        sqlStringBuilder.append(columnStr);
        sqlStringBuilder.append(") VALUES (");
        var valueStr = fieldList.stream().map(field -> String.format("#{%s}",field)).collect(joining(","));
        sqlStringBuilder.append(valueStr);
        sqlStringBuilder.append(")");
    }

    /**
     * 构建Delete 语句
     */
    private void buildDelete(){
        var tableInfo = getTableInfo(modelClass);
        var sql = String.format("DELETE %s WHERE Id = #{id}",tableInfo.getName());
        sqlStringBuilder.append("DELETE " + tableInfo.getName());
        if(query == null){
            sqlStringBuilder.append(" WHERE id = #{id}");
        }
    }

    /**
     * 构建Update 语句
     */
    private void buildUpdate(){
        var tableInfo = getTableInfo(modelClass);
        sqlStringBuilder.append(String.format("UPDATE %s SET ",tableInfo.getName()));
        var valueStr = getNotNullFields(model).stream().filter(p-> p != "id")
                .map(field -> {
                    var columnName = tableInfo.getFieldColumnMap().get(field).getName();
                    return String.format("%s = #{%s}",columnName,field);
                })
                .collect(joining(","));
        sqlStringBuilder.append(valueStr);
        if(query == null){
            sqlStringBuilder.append(" WHERE id = #{id}");
        }
    }

    /**
     * 构建Update 语句
     */
    private void buildSelect(){
        var tableInfo = getTableInfo(modelClass);
        var columnList = tableInfo.getFieldColumnMap().values()
                .stream()
                .map(columnInfo -> columnInfo.getName())
                .collect(toList());
        var columnStr = String.join(",",columnList);
        sqlStringBuilder.append(String.format("SELECT %s FROM %s ",columnStr,tableInfo.getName()));
        if(query == null){
            sqlStringBuilder.append("WHERE Id = #{id}");
        }
    }

    private final static List<String> PAGED_FIELDS = List.of("skip","take","orderBy");
    private final static String OR_SIGN = SqlConditionTypeEnum.OR.getName();
    private final static String AND_SIGN = SqlConditionTypeEnum.AND.getName();

    /**************************构建Sql子句******************************/
    /**
     * "Is NULL","LIKE","NOT LIKE","NOT IN","LIKE","EndsWith","From","To","GreaterThan","LessThan","NotEquals","Equals","In"
     * 构建Where子句
     */
    private void buildWhere(){
        TableInfo tableInfo = getTableInfo(modelClass);
        var queryMap = getNotNullQueryFieldMap(query);

        //List<String> conditionSigns = List.of("Is NULL","LIKE","NOT LIKE","NOT IN","LIKE","EndsWith","From","To","GreaterThan","LessThan","NotEquals","Equals","In");
        var andFields = queryMap.keySet().stream()
                .filter(p -> !PAGED_FIELDS.contains(p))
                .filter(p -> !p.startsWith("or"))
                .collect(toList());
        var orFields = queryMap.keySet().stream()
                .filter(p -> !PAGED_FIELDS.contains(p))
                .filter(p -> p.startsWith("or"))
                .collect(toList());

        if(andFields.size() > 0 && orFields.size() == 0){
            sqlStringBuilder.append("WHERE ");
            buildWhereAndCondition(tableInfo, queryMap,andFields);
        } else if(andFields.size() > 0 && orFields.size() > 0){
            sqlStringBuilder.append("WHERE ");
            buildWhereAndCondition(tableInfo, queryMap,andFields);
            sqlStringBuilder.insert(sqlStringBuilder.lastIndexOf("WHERE") + 6,"(").append(") ");
            sqlStringBuilder.append("OR (");
            buildWhereOrCondition(tableInfo, queryMap,orFields);
            sqlStringBuilder.append(")");
        } else if(andFields.size() == 0 && orFields.size() > 0){
            sqlStringBuilder.append("WHERE ");
            buildWhereOrCondition(tableInfo, queryMap,orFields);
        }
    }

    /**
     * 构建and 查询条件
     * @param tableInfo
     * @param queryMap
     * @param andFields
     */
    private void buildWhereAndCondition(TableInfo tableInfo,Map<String,Object> queryMap,List<String> andFields){
        for (var andField: andFields){
            var fieldName = tableInfo.getFieldColumnMap()
                    .keySet()
                    .stream()
                    .filter(p -> andField.contains(p))
                    .findFirst()
                    .orElse(null);
            var columnName = tableInfo.getFieldColumnMap().get(fieldName).getName();
            if( columnName != null){
                var filedValue = queryMap.get(andField);
                if (filedValue.getClass().isArray() && andField.endsWith("s")){
                    var valueArray = (Object[])filedValue;
                    var inSeq = "";
                    if(filedValue instanceof String[]){
                        inSeq = Arrays.stream(valueArray).map(p -> String.format("\'%s\'",p)).collect(joining(","));
                    } else if(filedValue instanceof Object[]){
                        inSeq = Arrays.stream(valueArray).map(p -> p.toString()).collect(joining(","));
                    }
                    sqlStringBuilder.append(String.format("%s in (%s) AND ",columnName,inSeq));
                } else if(andField.endsWith("NotEquals")) {
                    sqlStringBuilder.append(String.format("%s != #{%s} AND ",columnName,andField));
                } else if(andField.equals(fieldName) || andField.endsWith("Equals")){
                    sqlStringBuilder.append(String.format("%s = #{%s} AND ",columnName,andField));
                } else if(andField.endsWith("NotContains")) {
                    sqlStringBuilder.append(String.format("%s NOT LIKE CONCAT('%%',#{%s},'%%') AND ",columnName,andField));
                }
                else if(andField.endsWith("Contains")){
                    sqlStringBuilder.append(String.format("%s LIKE CONCAT('%%',#{%s},'%%') AND ",columnName,andField));
                } else if(andField.endsWith("NotIn")) {
                    var valueArray = (Object[])filedValue;
                    var inSeq = "";
                    if(filedValue instanceof String[]){
                        inSeq = Arrays.stream(valueArray).map(p -> String.format("'%s'",p)).collect(joining(","));
                    } else if(filedValue instanceof Object[]){
                        inSeq = Arrays.stream(valueArray).map(p -> p.toString()).collect(joining(","));
                    }
                    sqlStringBuilder.append(String.format("%s NOT IN (%s) AND ",columnName,inSeq));
                } else if(andField.endsWith("StartsWith")){
                    sqlStringBuilder.append(String.format("%s LIKE CONCAT(#{%s},'%%') AND ",columnName,andField));
                } else if(andField.endsWith("EndsWith")){
                    sqlStringBuilder.append(String.format("%s LIKE CONCAT('%%',#{%s}) AND ",columnName,andField));
                } else if(andField.endsWith("From")){
                    sqlStringBuilder.append(String.format("%s >= #{%s} AND ",columnName,andField));
                }else if(andField.endsWith("To")){
                    sqlStringBuilder.append(String.format("%s <= #{%s} AND ",columnName,andField));
                } else if(andField.endsWith("GreaterThan")){
                    sqlStringBuilder.append(String.format("%s > #{%s} AND ",columnName,andField));
                } else if(andField.endsWith("LessThan")){
                    sqlStringBuilder.append(String.format("%s < #{%s} AND ",columnName,andField));
                } else if(andField.endsWith("IsNull") && filedValue instanceof Boolean){
                    if ((Boolean)filedValue){
                        sqlStringBuilder.append(String.format("%s IS NULL AND ",columnName));
                    }else {
                        sqlStringBuilder.append(String.format("%s IS NOT NULL AND ",columnName));
                    }
                }
            }
        }
        sqlStringBuilder.delete(sqlStringBuilder.length()-5,sqlStringBuilder.length()-1);
    }

    private void buildWhereOrCondition(TableInfo tableInfo,Map<String,Object> queryMap,List<String> orFields){
        var queryInfo = getQueryInfo(query.getClass());
        for (var orField: orFields){
            var conditionName = queryInfo.getConditionNameMap().get(orField);
            var fieldName = tableInfo.getFieldColumnMap()
                    .keySet()
                    .stream()
                    .filter(p -> conditionName.contains(p))
                    .findFirst()
                    .orElse(null);
            var columnName = tableInfo.getFieldColumnMap().get(fieldName).getName();

            if( columnName != null){
                var filedValue = queryMap.get(orField);
                if (filedValue.getClass().isArray() && orField.endsWith("s")){
                    var valueArray = (Object[])filedValue;
                    var inSeq = "";
                    if(filedValue instanceof String[]){
                        inSeq = Arrays.stream(valueArray).map(p -> String.format("\'%s\'",p)).collect(joining(","));
                    } else if(filedValue instanceof Object[]){
                        inSeq = Arrays.stream(valueArray).map(p -> p.toString()).collect(joining(","));
                    }
                    sqlStringBuilder.append(String.format("%s in (%s) OR ",columnName,inSeq));
                } else if(orField.endsWith("NotEquals")) {
                    sqlStringBuilder.append(String.format("%s != #{%s} OR ",columnName,orField));
                } else if(conditionName.equals(fieldName) || orField.endsWith("Equals")){
                    sqlStringBuilder.append(String.format("%s = #{%s} OR ",columnName,orField));
                } else if(orField.endsWith("NotContains")) {
                    sqlStringBuilder.append(String.format("%s NOT LIKE CONCAT('%%',#{%s},'%%') OR ",columnName,orField));
                }
                else if(orField.endsWith("Contains")){
                    sqlStringBuilder.append(String.format("%s LIKE CONCAT('%%',#{%s},'%%') OR ",columnName,orField));
                } else if(orField.endsWith("NotIn")) {
                    var valueArray = (Object[])filedValue;
                    var inSeq = "";
                    if(filedValue instanceof String[]){
                        inSeq = Arrays.stream(valueArray).map(p -> String.format("'%s'",p)).collect(joining(","));
                    } else if(filedValue instanceof Object[]){
                        inSeq = Arrays.stream(valueArray).map(p -> p.toString()).collect(joining(","));
                    }
                    sqlStringBuilder.append(String.format("%s NOT IN (%s) OR ",columnName,inSeq));
                } else if(orField.endsWith("StartsWith")){
                    sqlStringBuilder.append(String.format("%s LIKE CONCAT(#{%s},'%%') OR ",columnName,orField));
                } else if(orField.endsWith("EndsWith")){
                    sqlStringBuilder.append(String.format("%s LIKE CONCAT('%%',#{%s}) OR ",columnName,orField));
                } else if(orField.endsWith("From")){
                    sqlStringBuilder.append(String.format("%s >= #{%s} OR ",columnName,orField));
                }else if(orField.endsWith("To")){
                    sqlStringBuilder.append(String.format("%s <= #{%s} OR ",columnName,orField));
                } else if(orField.endsWith("GreaterThan")){
                    sqlStringBuilder.append(String.format("%s > #{%s} OR ",columnName,orField));
                } else if(orField.endsWith("LessThan")){
                    sqlStringBuilder.append(String.format("%s < #{%s} OR ",columnName,orField));
                } else if(orField.endsWith("IsNull") && filedValue instanceof Boolean){
                    if ((Boolean)filedValue){
                        sqlStringBuilder.append(String.format("%s IS NULL OR ",columnName));
                    }else {
                        sqlStringBuilder.append(String.format("%s IS NOT NULL OR ",columnName));
                    }
                }
            }
        }
        sqlStringBuilder.delete(sqlStringBuilder.length()-4,sqlStringBuilder.length()-1);
    }

    /**
     * 构建OrderBy子句
     */
    private void buildOrderBy(){
        var queryInfo = getQueryInfo(query.getClass());
        var oderByField = queryInfo.getGetMethodMap().keySet().stream()
                .filter(p -> p.equals("orderBy"))
                .findFirst().orElse(null);

        if(oderByField != null){
            try {
                var fieldValue = queryInfo.getGetMethodMap().get(oderByField).invoke(query);
                if(fieldValue != null){
                    sqlStringBuilder.append(" ORDER BY " + fieldValue);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 构建Top子句
     */
    private void buildTop(){
        var queryInfo = getQueryInfo(query.getClass());
        var takeField = queryInfo.getGetMethodMap().keySet().stream()
                .filter(p -> p.equals("take"))
                .findFirst().orElse(null);

        if(takeField != null){
            try {
                var fieldValue = queryInfo.getGetMethodMap().get(takeField).invoke(query);
                if(fieldValue != null){
                    var topSql = String.format(" TOP(%s)",fieldValue);
                    sqlStringBuilder.insert(sqlStringBuilder.indexOf("SELECT") + 6,topSql);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 构建OffsetFetch子句
     */
    private void buildOffsetFetch(){
        var queryInfo = getQueryInfo(query.getClass());
        var orderByField = queryInfo.getGetMethodMap().keySet().stream()
                .filter(p -> p.equals("orderBy"))
                .findFirst().orElse(null);
        var takeField = queryInfo.getGetMethodMap().keySet().stream()
                .filter(p -> p.equals("take"))
                .findFirst().orElse(null);
        var skipField = queryInfo.getGetMethodMap().keySet().stream()
                .filter(p -> p.equals("skip"))
                .findFirst().orElse(null);
        try {
            var orderByFieldValue = queryInfo.getGetMethodMap().get(orderByField).invoke(query);
            var takeFieldValue = queryInfo.getGetMethodMap().get(takeField).invoke(query);
            var skipFieldValue = queryInfo.getGetMethodMap().get(skipField).invoke(query);

            if(sqlStringBuilder.indexOf("ORDER BY") < 0){
                if (orderByFieldValue != null){
                    sqlStringBuilder.append(" ORDER BY " + orderByFieldValue);
                }else {
                    sqlStringBuilder.append(" ORDER BY Id ");
                }
            }
            if(takeFieldValue != null){
                sqlStringBuilder.append(String.format(" OFFSET %d ROWS FETCH NEXT %d ROWS ONLY",skipFieldValue,takeFieldValue));
            } else if(skipFieldValue != null){
                sqlStringBuilder.append(String.format(" OFFSET %d ROWS",skipFieldValue));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 构建GroupBy子句
     */
    private void buildGroupBy(){
        sqlStringBuilder.append("Group By");
    }

    /**
     * 构建Having子句
     */
    private void buildHaving(){
        sqlStringBuilder.append("Having ");
    }
}
