package com.gmfiot.data;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableInfo {
    private String tableName;
    private List<String> columns;
    private Map<String, Method> getMethodMap;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public Map<String, Method> getGetMethodMap() {
        return getMethodMap;
    }

    public void setGetMethodMap(Map<String, Method> getMethodMap) {
        this.getMethodMap = getMethodMap;
    }
}