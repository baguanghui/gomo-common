package com.gmfiot.data;

import com.gmfiot.core.data.Paged;
import com.gmfiot.core.data.Query;
import com.gmfiot.core.model.BaseModel;

import java.util.List;

/**
 * 数据库访问接口
 * @author ThinkPad
 */
public interface BaseMapper<T extends BaseModel> {

    Integer insert(T entity);

    Integer updateById(T entity);

    Integer updateByQuery(T entity, Query query);

    Integer deleteById(long id,Class<?> clazz);

    Integer deleteByQuery(Query query);

    T selectById(Long id,Class<?> clazz);

    List<T> selectList(Query query);

    Paged<T> selectPaged(Query query);

    Integer selectCount(Object query);
}
