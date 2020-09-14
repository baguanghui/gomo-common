package com.gmfiot.data.sql;

public interface ResultSetToModel<T> {
    T toMapModel(Object object,Class<?> modelClass);
}
