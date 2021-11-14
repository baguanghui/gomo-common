package com.gmfiot.data;

import com.gmfiot.core.data.Paged;
import com.gmfiot.core.util.ReflectionUtil;
import com.gmfiot.data.sql.SqlPlaceholderEnum;
import com.gmfiot.data.sql.SqlServerSqlBuilder;
import com.gmfiot.data.sql.SqlTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author BaGuangHui
 */
@Component
public class SqlServerDataProvider implements DataProvider {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Override
    public NamedParameterJdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    @Override
    public TransactionTemplate getTransactionTemplate() {
        return transactionTemplate;
    }

    @Override
    public <T> Integer insert(T entity) {
        var sql = SqlServerSqlBuilder.getBuilder(entity)
                .setSqlPlaceholder(SqlPlaceholderEnum.COLON)
                .build(SqlTypeEnum.INSERT)
                .toString();

        var paramMap = ReflectionUtil.getNotNullFieldValueMap(entity);
        return jdbcTemplate.update(sql,paramMap);
    }

    @Override
    public <T> Integer insert(ArrayList<T> entities) {
        var firstEntity = entities.stream().findFirst();
        if(firstEntity.isEmpty()){
            throw new RuntimeException("列表不能为空");
        }
        var affectRows =  executeInTransaction(transactionStatus -> {
            var count = 0;
            for(var entity : entities)  {
                insert(entity);
                count++;
            }
            return count;
        });
        return affectRows;
    }

    @Override
    public <T> Integer update(T entity) {
        var sql = SqlServerSqlBuilder.getBuilder(entity)
                .setSqlPlaceholder(SqlPlaceholderEnum.COLON)
                .build(SqlTypeEnum.UPDATE)
                .toString();
        var paramMap = ReflectionUtil.getNotNullFieldValueMap(entity);
        return jdbcTemplate.update(sql,paramMap);
    }

    @Override
    public Integer delete(Long id, Class modelClass) {
        var sql = SqlServerSqlBuilder.getBuilder(modelClass)
                .setSqlPlaceholder(SqlPlaceholderEnum.COLON)
                .build(SqlTypeEnum.DELETE)
                .toString();
        var paramMap = Map.of("id", id);
        return jdbcTemplate.update(sql,paramMap);
    }

    @Override
    public Integer delete(Object query, Class modelClass) {
        var sql = SqlServerSqlBuilder.getBuilder(modelClass,query)
                .setSqlPlaceholder(SqlPlaceholderEnum.COLON)
                .build(SqlTypeEnum.DELETE)
                .build(SqlTypeEnum.WHERE)
                .toString();
        var paramMap = ReflectionUtil.getNotNullFieldValueMap(query);
        return jdbcTemplate.update(sql,paramMap);
    }

    @Override
    public <T> T select(Long id, Class<T> modelClass) {
        var sql = SqlServerSqlBuilder.getBuilder(modelClass)
                .setSqlPlaceholder(SqlPlaceholderEnum.COLON)
                .build(SqlTypeEnum.SELECT)
                .toString();
        var paramMap = Map.of("id", id);
        return jdbcTemplate.queryForObject(sql, paramMap, new ModelPropertyRowMapper<T>(modelClass));
    }

    @Override
    public <T> List<T> select(Object query, Class<T> modelClass) {
        var sql = SqlServerSqlBuilder.getBuilder(modelClass,query)
                .setSqlPlaceholder(SqlPlaceholderEnum.COLON)
                .build(SqlTypeEnum.SELECT)
                .build(SqlTypeEnum.WHERE)
                .build(SqlTypeEnum.ORDER_BY)
                .toString();
        var paramMap = ReflectionUtil.getNotNullFieldValueMap(query);
        return jdbcTemplate.query(sql, paramMap, new ModelResultSetExtractor<T>(modelClass));
    }

    @Override
    public <T> List<T> select(String sql, Object query, Class<T> modelClass) {
        var paramMap = ReflectionUtil.getNotNullFieldValueMap(query);
        return jdbcTemplate.query(sql, paramMap, new ModelResultSetExtractor<T>(modelClass));
    }

    @Override
    public <T> Paged<T> selectPaged(Object query, Class<T> modelClass) {
        var sql = SqlServerSqlBuilder.getBuilder(modelClass,query)
                .setSqlPlaceholder(SqlPlaceholderEnum.COLON)
                .build(SqlTypeEnum.SELECT)
                .build(SqlTypeEnum.WHERE)
                .build(SqlTypeEnum.ORDER_BY)
                .build(SqlTypeEnum.OFFSET_FETCH)
                .toString();
        var paramMap = ReflectionUtil.getNotNullFieldValueMap(query);
        var modelList = jdbcTemplate.query(sql,paramMap,new ModelResultSetExtractor<T>(modelClass));
        var modelCount = count(query,modelClass);
        var pagedResult = new Paged<T>(modelList,modelCount);
        return pagedResult;
    }

    @Override
    public <T> Paged<T> selectPaged(String sql, Object query, Class<T> modelClass) {
        var paramMap = ReflectionUtil.getNotNullFieldValueMap(query);
        var modelList = jdbcTemplate.query(sql,paramMap,new ModelResultSetExtractor<T>(modelClass));
        var modelCount = count(query,modelClass);
        var pagedResult = new Paged<T>(modelList,modelCount);
        return pagedResult;
    }

    @Override
    public Integer count(Object query, Class<?> modelClass) {
        var sql = SqlServerSqlBuilder.getBuilder(modelClass,query)
                .setSqlPlaceholder(SqlPlaceholderEnum.COLON)
                .build(SqlTypeEnum.COUNT)
                .build(SqlTypeEnum.WHERE)
                .toString();
        var paramMap = ReflectionUtil.getNotNullFieldValueMap(query);
        return jdbcTemplate.queryForObject(sql,paramMap,Integer.class);
    }

    @Override
    public Long getId(){
        var sql = "SELECT NEXT VALUE FOR Ids";
        Map map = null;
        return jdbcTemplate.queryForObject(sql,map,Long.class);
    }

    public <T> T executeInTransaction(TransactionCallback<T> transactionCallback){
        return transactionTemplate.execute(transactionCallback);
    }

    public void executeInTransaction(Consumer<TransactionStatus> action){
        transactionTemplate.executeWithoutResult(action);
    }
}
