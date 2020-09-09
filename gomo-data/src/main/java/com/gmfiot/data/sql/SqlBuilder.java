package com.gmfiot.data.sql;

public interface SqlBuilder {
    SqlBuilder build(SqlTypeEnum sqlTypeEnum);
    SqlBuilder build(SqlClauseTypeEnum sqlClauseTypeEnum);
    @Override
    String toString();
}
