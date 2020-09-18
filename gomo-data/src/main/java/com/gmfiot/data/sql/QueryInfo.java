package com.gmfiot.data.sql;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * @author BaGuangHui
 */
public class QueryInfo {
    private String typeName;
    private List<String> fields;
    private Map<String, Method> readMethodMap;
    private Map<String,String> conditionNameMap;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public Map<String, Method> getReadMethodMap() {
        return readMethodMap;
    }

    public void setReadMethodMap(Map<String, Method> readMethodMap) {
        this.readMethodMap = readMethodMap;
    }

    public Map<String, String> getConditionNameMap() {
        return conditionNameMap;
    }

    public void setConditionNameMap(Map<String, String> conditionNameMap) {
        this.conditionNameMap = conditionNameMap;
    }
}