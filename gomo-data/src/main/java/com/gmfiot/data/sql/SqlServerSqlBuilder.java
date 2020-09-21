package com.gmfiot.data.sql;
import com.gmfiot.core.BusinessException;
import com.gmfiot.core.util.StringUtil;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

/**
 * @author BaGuangHui
 */
public class SqlServerSqlBuilder implements SqlBuilder {

    private StringBuilder sqlStringBuilder;
    private Class modelClass;
    private Object model;
    private Object query;
    //?,:,#,nothing
    private SqlPlaceholderEnum sqlPlaceholderEnum;

    public SqlServerSqlBuilder(){}

    public SqlServerSqlBuilder(Class modelClass, Object model, Object query){
        this.modelClass = modelClass;
        this.model = model;
        this.query = query;
        this.sqlStringBuilder = new StringBuilder();
        if(modelClass == null && model == null ){
            if(query == null){
                throw new BusinessException("initial parameters is invalid");
            }
            var queryInfo = SqlMappingData.getQueryInfo(query.getClass());
            var getModelClass = queryInfo.getReadMethodMap().get("modelClass");
            if(getModelClass == null){
                throw new BusinessException("modelClass field is missing");
            }
            try {
                var modelClassObj = getModelClass.invoke(query);
                if(modelClassObj != null){
                    this.modelClass = (Class) modelClassObj;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        this.sqlPlaceholderEnum = SqlPlaceholderEnum.QUESTION_MARK;
    }

    public SqlServerSqlBuilder(Class modelClass, Object model, Object query,SqlPlaceholderEnum sqlPlaceholderEnum){
        this(modelClass,model,query);
        this.sqlPlaceholderEnum = sqlPlaceholderEnum;
    }

    @Override
    public SqlBuilder setSqlPlaceholder(SqlPlaceholderEnum sqlPlaceholderEnum) {
        this.sqlPlaceholderEnum = sqlPlaceholderEnum;
        return this;
    }

    @Override
    public SqlPlaceholderEnum getSqlPlaceholder() {
        return this.sqlPlaceholderEnum;
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
            case COUNT:
                buildCount();
                break;
            case WHERE:
                buildWhere();
                break;
            case ORDER_BY:
                buildOrderBy();
                break;
            case DISTINCT:
                buildDistinct();
                break;
            case TOP:
                buildTop();
                break;
            case OFFSET_FETCH:
                buildOffsetFetch();
                break;
            case GROUP_BY:
                buildGroupBy();
                break;
            case HAVING:
                buildHaving();
                break;
            default:
                throw new UnsupportedOperationException("Unsupported SqlTypeEnum");
        }
        return this;
    }

    public static <M, Q> SqlBuilder getBuilder(M model, Q query) {
        var sqlBuilder = new SqlServerSqlBuilder(model.getClass(),model,query);
        return sqlBuilder;
    }

    public static <M, Q> SqlBuilder getBuilder(Class<M> modelClass, Q query) {
        var sqlBuilder = new SqlServerSqlBuilder(modelClass,null,query);
        return sqlBuilder;
    }

    public static <M> SqlBuilder getBuilder(M model) {
        var sqlBuilder = new SqlServerSqlBuilder(model.getClass(),model,null);
        return sqlBuilder;
    }

    public static <M> SqlBuilder getBuilder(Class<M> modelClass) {
        var sqlBuilder = new SqlServerSqlBuilder(modelClass,null,null);
        return sqlBuilder;
    }

    public static SqlBuilder getBuilderForQuery(Object query) {
        var sqlBuilder = new SqlServerSqlBuilder(null,null,query);
        return sqlBuilder;
    }

    @Override
    public String toString() {
        return sqlStringBuilder.toString();
    }

    /**************************构建Sql语句******************************/

    private String getSqlPlaceholderSign(String filedName){
        if(sqlPlaceholderEnum == SqlPlaceholderEnum.QUESTION_MARK){
            return "?";
        } else if(sqlPlaceholderEnum == SqlPlaceholderEnum.COLON){
            return String.format(":%s",filedName);
        } else if(sqlPlaceholderEnum == SqlPlaceholderEnum.HASH_SIGN){
            return String.format("#{%s}",filedName);
        }
        return null;
    }

    /**
     * 构建insert 语句
     */
    private void buildInsert(){
        var tableInfo = SqlMappingData.getTableInfo(modelClass);
        sqlStringBuilder.append(String.format("INSERT INTO %s(",tableInfo.getName()));
        var fieldList = SqlMappingData.getNotNullFields(model);
        var columnList = fieldList.stream().map(field -> tableInfo.getFieldColumnMap().get(field).getName()).collect(toList());
        var columnStr = String.join(",",columnList);
        sqlStringBuilder.append(columnStr);
        sqlStringBuilder.append(") VALUES (");
        String valueStr = fieldList.stream().map(field -> getSqlPlaceholderSign(field)).collect(joining(","));;
        sqlStringBuilder.append(valueStr);
        sqlStringBuilder.append(")");
    }

    /**
     * 构建Delete 语句
     */
    private void buildDelete(){
        var tableInfo = SqlMappingData.getTableInfo(modelClass);
        sqlStringBuilder.append("DELETE ");
        sqlStringBuilder.append(tableInfo.getName());
        sqlStringBuilder.append(" ");
        if(query == null){
            sqlStringBuilder.append("WHERE id = ");
            sqlStringBuilder.append(getSqlPlaceholderSign("id"));
        }
    }

    /**
     * 构建Update 语句
     */
    private void buildUpdate(){
        var tableInfo = SqlMappingData.getTableInfo(modelClass);
        sqlStringBuilder.append(String.format("UPDATE %s SET ",tableInfo.getName()));
        var valueStr = SqlMappingData.getNotNullFields(model).stream().filter(p-> p != "id")
                .map(field -> {
                    var columnName = tableInfo.getFieldColumnMap().get(field).getName();
                    return String.format("%s = %s",columnName, getSqlPlaceholderSign(field));
                    //return String.format("%s = #{%s}",columnName,field);
                })
                .collect(joining(","));
        sqlStringBuilder.append(valueStr);
        if(query == null){
            sqlStringBuilder.append(" WHERE id = ");
            sqlStringBuilder.append(getSqlPlaceholderSign("id"));
        }
    }

    /**
     * 构建Select 语句
     */
    private void buildSelect(){
        var tableInfo = SqlMappingData.getTableInfo(modelClass);
        var columnList = tableInfo.getFieldColumnMap().values()
                .stream()
                .map(columnInfo -> columnInfo.getName())
                .collect(toList());
        var columnStr = String.join(",",columnList);
        sqlStringBuilder.append(String.format("SELECT %s FROM %s ",columnStr,tableInfo.getName()));
        if(query == null){
            sqlStringBuilder.append("WHERE Id = ");
            sqlStringBuilder.append(getSqlPlaceholderSign("id"));
        }
    }

    /**
     * 构建buildCount 语句
     */
    private void buildCount(){
        var tableInfo = SqlMappingData.getTableInfo(modelClass);
        sqlStringBuilder.append(String.format("SELECT COUNT(*) FROM %s ",tableInfo.getName()));
    }

    private final static List<String> PAGED_FIELDS = List.of("skip","take","orderBy","modelClass","groupBy","having");
//    private final static String OR_SIGN = SqlConditionTypeEnum.OR.getName();
//    private final static String AND_SIGN = SqlConditionTypeEnum.AND.getName();

    /**************************构建Sql子句******************************/
    /**
     * "Is NULL","LIKE","NOT LIKE","NOT IN","LIKE","EndsWith","From","To","GreaterThan","LessThan","NotEquals","Equals","In"
     * 构建Where子句
     */
    private void buildWhere(){
        TableInfo tableInfo = SqlMappingData.getTableInfo(modelClass);
        var queryMap = SqlMappingData.getNotNullQueryFieldMap(query);

        var andFields = queryMap.keySet().stream()
                .filter(p -> !PAGED_FIELDS.contains(p))
                .filter(p -> !p.startsWith("or"))
                //.filter(p -> !p.endsWith("GroupBy"))
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
            if(fieldName == null){
                continue;
            }
            var columnInfo =  tableInfo.getFieldColumnMap().get(fieldName);
            if(columnInfo == null){
                continue;
            }
            var columnName = columnInfo.getName();
            var filedValue = queryMap.get(andField);
            if (filedValue.getClass().isArray() && andField.endsWith("s")){
                var valueArray = (Object[])filedValue;
                var inSeq = new StringBuilder();
                if(filedValue instanceof Object[]){
                    for (int i = 0; i < valueArray.length; i++) {
                        inSeq.append(getSqlPlaceholderSign(andField + i));
                        inSeq.append(",");
                    }
                    if(valueArray.length > 0){
                        inSeq.deleteCharAt(inSeq.length() - 1);
                    }
                }
                sqlStringBuilder.append(String.format("%s in (%s) AND ",columnName,inSeq));
            } else if(andField.endsWith("NotEquals")) {
                sqlStringBuilder.append(String.format("%s != %s AND ",columnName, getSqlPlaceholderSign(andField)));
            } else if(andField.equals(fieldName) || andField.endsWith("Equals")){
                sqlStringBuilder.append(String.format("%s = %s AND ",columnName, getSqlPlaceholderSign(andField)));
            } else if(andField.endsWith("NotContains")) {
                sqlStringBuilder.append(String.format("%s NOT LIKE CONCAT('%%',%,'%%') AND ",columnName,getSqlPlaceholderSign(andField)));
            }
            else if(andField.endsWith("Contains")){
                sqlStringBuilder.append(String.format("%s LIKE CONCAT('%%',%s,'%%') AND ",columnName,getSqlPlaceholderSign(andField)));
            } else if(andField.endsWith("NotIn")) {
                var valueArray = (Object[])filedValue;
//                var inSeq = "";
//                if(filedValue instanceof String[]){
//                    inSeq = Arrays.stream(valueArray).map(p -> String.format("'%s'",p)).collect(joining(","));
//                } else if(filedValue instanceof Object[]){
//                    inSeq = Arrays.stream(valueArray).map(p -> p.toString()).collect(joining(","));
//                }
                var inSeq = new StringBuilder();
                if(filedValue instanceof Object[]){
                    for (int i = 0; i < valueArray.length; i++) {
                        inSeq.append(getSqlPlaceholderSign(andField + i));
                        inSeq.append(",");
                    }
                    if(valueArray.length > 0){
                        inSeq.deleteCharAt(inSeq.length() - 1);
                    }
                }
                sqlStringBuilder.append(String.format("%s NOT IN (%s) AND ",columnName,inSeq));
            } else if(andField.endsWith("StartsWith")){
                sqlStringBuilder.append(String.format("%s LIKE CONCAT(%s,'%%') AND ",columnName,getSqlPlaceholderSign(andField)));
            } else if(andField.endsWith("EndsWith")){
                sqlStringBuilder.append(String.format("%s LIKE CONCAT('%%',%s) AND ",columnName,getSqlPlaceholderSign(andField)));
            } else if(andField.endsWith("From")){
                sqlStringBuilder.append(String.format("%s >= %s AND ",columnName,getSqlPlaceholderSign(andField)));
            }else if(andField.endsWith("To")){
                sqlStringBuilder.append(String.format("%s <= %s AND ",columnName,getSqlPlaceholderSign(andField)));
            } else if(andField.endsWith("GreaterThan")){
                sqlStringBuilder.append(String.format("%s > %s AND ",columnName,getSqlPlaceholderSign(andField)));
            } else if(andField.endsWith("LessThan")){
                sqlStringBuilder.append(String.format("%s < %s AND ",columnName,getSqlPlaceholderSign(andField)));
            } else if(andField.endsWith("IsNull") && filedValue instanceof Boolean){
                if ((Boolean)filedValue){
                    sqlStringBuilder.append(String.format("%s IS NULL AND ",columnName));
                }else {
                    sqlStringBuilder.append(String.format("%s IS NOT NULL AND ",columnName));
                }
            }
        }
        sqlStringBuilder.delete(sqlStringBuilder.length()-5,sqlStringBuilder.length()-1);
    }

    private void buildWhereOrCondition(TableInfo tableInfo,Map<String,Object> queryMap,List<String> orFields){
        var queryInfo = SqlMappingData.getQueryInfo(query.getClass());
        for (var orField: orFields){
            var conditionName = queryInfo.getConditionNameMap().get(orField);
            var fieldName = tableInfo.getFieldColumnMap()
                    .keySet()
                    .stream()
                    .filter(p -> conditionName.contains(p))
                    .findFirst()
                    .orElse(null);

            if(fieldName == null){
                continue;
            }
            var columnInfo =  tableInfo.getFieldColumnMap().get(fieldName);
            if(columnInfo == null){
                continue;
            }
            var columnName = columnInfo.getName();

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
                sqlStringBuilder.append(String.format("%s != %s OR ",columnName,getSqlPlaceholderSign(orField)));
            } else if(conditionName.equals(fieldName) || orField.endsWith("Equals")){
                sqlStringBuilder.append(String.format("%s = %s OR ",columnName,getSqlPlaceholderSign(orField)));
            } else if(orField.endsWith("NotContains")) {
                sqlStringBuilder.append(String.format("%s NOT LIKE CONCAT('%%',%s,'%%') OR ",columnName,getSqlPlaceholderSign(orField)));
            }
            else if(orField.endsWith("Contains")){
                sqlStringBuilder.append(String.format("%s LIKE CONCAT('%%',%s,'%%') OR ",columnName,getSqlPlaceholderSign(orField)));
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
                sqlStringBuilder.append(String.format("%s LIKE CONCAT(%s,'%%') OR ",columnName,getSqlPlaceholderSign(orField)));
            } else if(orField.endsWith("EndsWith")){
                sqlStringBuilder.append(String.format("%s LIKE CONCAT('%%',%s) OR ",columnName,getSqlPlaceholderSign(orField)));
            } else if(orField.endsWith("From")){
                sqlStringBuilder.append(String.format("%s >= %s OR ",columnName,getSqlPlaceholderSign(orField)));
            }else if(orField.endsWith("To")){
                sqlStringBuilder.append(String.format("%s <= %s OR ",columnName,getSqlPlaceholderSign(orField)));
            } else if(orField.endsWith("GreaterThan")){
                sqlStringBuilder.append(String.format("%s > %s OR ",columnName,getSqlPlaceholderSign(orField)));
            } else if(orField.endsWith("LessThan")){
                sqlStringBuilder.append(String.format("%s < %s OR ",columnName,getSqlPlaceholderSign(orField)));
            } else if(orField.endsWith("IsNull") && filedValue instanceof Boolean){
                if ((Boolean)filedValue){
                    sqlStringBuilder.append(String.format("%s IS NULL OR ",columnName));
                }else {
                    sqlStringBuilder.append(String.format("%s IS NOT NULL OR ",columnName));
                }
            }
        }
        sqlStringBuilder.delete(sqlStringBuilder.length()-4,sqlStringBuilder.length()-1);
    }

    /**
     * 构建OrderBy子句
     */
    private void buildOrderBy(){
        var queryInfo = SqlMappingData.getQueryInfo(query.getClass());
        var oderByField = queryInfo.getReadMethodMap().keySet().stream()
                .filter(p -> p.equals("orderBy"))
                .findFirst().orElse(null);

        if(oderByField != null){
            try {
                var fieldValue = queryInfo.getReadMethodMap().get(oderByField).invoke(query);
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
     * 构建Distinct子句
     */
    private void buildDistinct(){
        int selectIndex = sqlStringBuilder.indexOf("SELECT");
        if(selectIndex >= 0){
            sqlStringBuilder.insert(selectIndex + 6," DISTINCT");
        } else {
            throw new BusinessException("The SELECT statement has not yet been built");
        }
    }

    /**
     * 构建Top子句
     */
    private void buildTop(){
        int selectIndex = sqlStringBuilder.indexOf("SELECT");
        if(selectIndex < 0){
            throw new BusinessException("The SELECT statement has not yet been built");
        }

        var queryInfo = SqlMappingData.getQueryInfo(query.getClass());
        var takeField = queryInfo.getReadMethodMap().keySet().stream()
                .filter(p -> p.equals("take"))
                .findFirst().orElse(null);

        if(takeField != null){
            try {
                var fieldValue = queryInfo.getReadMethodMap().get(takeField).invoke(query);
                if(fieldValue != null){
                    var topSql = String.format(" TOP(%s)",fieldValue);
                    sqlStringBuilder.insert(selectIndex + 6,topSql);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 构建OffsetFetch子句
     */
    private void buildOffsetFetch(){
        var queryInfo = SqlMappingData.getQueryInfo(query.getClass());
        var orderByField = queryInfo.getReadMethodMap().keySet().stream()
                .filter(p -> p.equals("orderBy"))
                .findFirst().orElse(null);
        var takeField = queryInfo.getReadMethodMap().keySet().stream()
                .filter(p -> p.equals("take"))
                .findFirst().orElse(null);
        var skipField = queryInfo.getReadMethodMap().keySet().stream()
                .filter(p -> p.equals("skip"))
                .findFirst().orElse(null);
        try {
            var orderByFieldValue = queryInfo.getReadMethodMap().get(orderByField).invoke(query);
            var takeFieldValue = queryInfo.getReadMethodMap().get(takeField).invoke(query);
            var skipFieldValue = queryInfo.getReadMethodMap().get(skipField).invoke(query);

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
        var queryInfo = SqlMappingData.getQueryInfo(query.getClass());
        var groupByFields = queryInfo.getReadMethodMap().keySet().stream()
                .filter(p -> p.equals("groupBy") || p.endsWith("GroupBy"))
                .collect(toList());

        for (var groupByField : groupByFields){
            try {
                var fieldValue = (String)queryInfo.getReadMethodMap().get(groupByField).invoke(query);
                if(fieldValue == null)
                {
                    continue;
                }
                int selectIndex = sqlStringBuilder.indexOf("SELECT");
                if(selectIndex < 0){
                    throw new BusinessException("The SELECT statement has not yet been built");
                }
                var aggregateFunctions = List.of("count","sum","max","min","avg");
                var rawGroupByFields = groupByField.replaceAll("GroupBy","").split("_");
                if(rawGroupByFields == null){

                    int fromIndex = sqlStringBuilder.indexOf("FROM");
                    sqlStringBuilder.replace(selectIndex + 7,fromIndex,fieldValue);
                    sqlStringBuilder.append(" GROUP BY " + fieldValue);
                    return;
                }
                StringBuilder selectedFields = new StringBuilder();
                selectedFields.append(fieldValue);
                if(!StringUtil.isBlank(fieldValue)){
                    selectedFields.append(",");
                }
                for(var rawGroupByField : rawGroupByFields){
                    var aggregateFunctionName = aggregateFunctions.stream().filter(p -> rawGroupByField.startsWith(p)).findFirst().orElse(null);
                    if(aggregateFunctionName == null){
                        return;
                    }
                    var filedNameForAggregation = rawGroupByField.substring(aggregateFunctionName.length(),rawGroupByField.indexOf("As"));
                    if(filedNameForAggregation.equals("Star")){
                        filedNameForAggregation = "*";
                    }
                    var fieldAliasName = rawGroupByField.substring(rawGroupByField.indexOf("As") + 2);
                    selectedFields.append(aggregateFunctionName.toUpperCase());
                    selectedFields.append("(");
                    selectedFields.append(filedNameForAggregation);
                    selectedFields.append(") As ");
                    selectedFields.append(fieldAliasName);
                    selectedFields.append(" ,");
                }
                selectedFields.deleteCharAt(selectedFields.length() - 1);
                int fromIndex = sqlStringBuilder.indexOf("FROM");
                sqlStringBuilder.replace(selectIndex + 7,fromIndex,selectedFields.toString());
                if(!StringUtil.isBlank(fieldValue)){
                    sqlStringBuilder.append(" GROUP BY " + fieldValue);
                }
                break;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 构建Having子句
     */
    private void buildHaving(){
        var queryInfo = SqlMappingData.getQueryInfo(query.getClass());
        var havingField = queryInfo.getReadMethodMap().keySet().stream()
                .filter(p -> p.equals("having"))
                .findFirst().orElse(null);

        if(havingField != null){
            try {
                var fieldValue = queryInfo.getReadMethodMap().get(havingField).invoke(query);
                if(fieldValue != null){
                    sqlStringBuilder.append(" HAVING " + fieldValue);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
