package com.gmfiot.data;

import com.gmfiot.core.data.Paged;
import com.gmfiot.core.data.Query;
import com.gmfiot.core.model.BaseModel;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

/**
 * 数据库访问接口
 * @author ThinkPad
 */
public interface DataProvider {

    default <E extends BaseModel> E insert(E entity){
        throw new UnsupportedOperationException();
    }

    default  <E extends BaseModel> E update(E entity){
        throw new UnsupportedOperationException();
    }

    default  <E extends BaseModel> E delete(long id){
        throw new UnsupportedOperationException();
    }

    default  <E extends BaseModel,T extends Query> E delete(T Query){
        throw new UnsupportedOperationException();
    }

    default  <E extends BaseModel> E select(long id){
        throw new UnsupportedOperationException();
    }
    default <E extends BaseModel,T extends Query> E[] select(T query){
        throw new UnsupportedOperationException();
    }
    default <E extends BaseModel,T extends Query> Paged<E> selectPaged(T query){
        throw new UnsupportedOperationException();
    }

    default Integer Count(Object query){
        throw new UnsupportedOperationException();
    }

    default Long getId(){
        throw new UnsupportedOperationException();
    }

    default NamedParameterJdbcTemplate getNamedParameterJdbcTemplate(){
        return  null;
    }
}
