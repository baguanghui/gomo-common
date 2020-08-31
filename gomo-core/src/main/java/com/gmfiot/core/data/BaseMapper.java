package com.gmfiot.core.data;

import com.gmfiot.core.model.BaseModel;

import java.util.List;

/**
 * 数据库访问接口
 * @author ThinkPad
 */
public interface BaseMapper<T extends BaseModel> {

    void insert(T entity);

    void updateById(T entity);

    void updateByQuery(T entity,Query query);

    void deleteById(long id);

    void deleteByQuery(Query query);

    T selectById(Long id);

    List<T> selectList(Query query);

    Paged<T> selectPaged(Query query);

    Integer selectCount(Object query);
}
