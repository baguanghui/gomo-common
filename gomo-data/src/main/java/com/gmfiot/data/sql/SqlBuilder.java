package com.gmfiot.data.sql;

public interface SqlBuilder {
    SqlBuilder build(SqlTypeEnum sqlTypeEnum);
    @Override
    String toString();
}
