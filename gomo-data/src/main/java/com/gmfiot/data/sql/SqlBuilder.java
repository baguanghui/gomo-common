package com.gmfiot.data.sql;

/**
 * @author BaGuangHui
 */
public interface SqlBuilder {
    SqlBuilder build(SqlTypeEnum sqlTypeEnum);
    SqlBuilder setSqlPlaceholder(SqlPlaceholderEnum sqlPlaceholderEnum);
    SqlPlaceholderEnum getSqlPlaceholder();
}
