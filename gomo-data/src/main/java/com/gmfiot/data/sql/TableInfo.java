package com.gmfiot.data.sql;

import java.util.Map;

public class TableInfo {
    private String name;
    private String schema;

    private Map<String,ColumnInfo> fieldColumnMap;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public Map<String, ColumnInfo> getFieldColumnMap() {
        return fieldColumnMap;
    }

    public void setFieldColumnMap(Map<String, ColumnInfo> fieldColumnMap) {
        this.fieldColumnMap = fieldColumnMap;
    }
}