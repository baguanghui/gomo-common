package com.gmfiot.data;

import com.gmfiot.core.data.Paged;
import com.gmfiot.core.data.Query;
import com.gmfiot.core.model.BaseModel;

import java.util.List;
import java.util.Map;

/**
 * 数据库访问接口
 * @author BaGuangHui
 */
public interface BaseMapper<T> {

    Integer insert(T entity);

    Integer updateById(T entity);

    Integer updateByQuery(T entity, Object query);

    Integer deleteById(long id);

    Integer deleteByQuery(Object query);

    T selectById(Long id);

    List<T> selectList(Object query);

    Paged selectPaged(Object query);

    Integer selectCount(Object query);
}
