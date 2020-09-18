package com.gmfiot.data;

import com.gmfiot.core.data.Paged;
import com.gmfiot.core.data.Query;
import com.gmfiot.core.model.BaseModel;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

/**
 * 数据库访问接口
 * @author BaGuangHui
 */
public interface DataProvider {

    <T> Integer insert(T entity);

    <T> Integer update(T entity);

    Integer delete(Long id,Class modelClass);

    Integer delete(Object query,Class modelClass);

    <T> T select(Long id,Class<T> modelClass);

    <T> List<T> select(Object query,Class<T> modelClass);

    <T> Paged<T> selectPaged(Object query,Class<T> modelClass);

    Integer Count(Object query,Class<?> modelClass);

    Long getId();

    default NamedParameterJdbcTemplate getJdbcTemplate(){
        return null;
    }

    default TransactionTemplate getTransactionTemplate(){ return null;}
}
