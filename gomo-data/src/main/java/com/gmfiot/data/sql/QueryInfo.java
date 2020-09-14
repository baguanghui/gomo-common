package com.gmfiot.data.sql;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class QueryInfo {
    private String typeName;
    private Class modelClass;
    private List<String> fields;
    private Map<String, Method> getMethodMap;
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

    public Map<String, Method> getGetMethodMap() {
        return getMethodMap;
    }

    public void setGetMethodMap(Map<String, Method> getMethodMap) {
        this.getMethodMap = getMethodMap;
    }

    public Map<String, String> getConditionNameMap() {
        return conditionNameMap;
    }

    public void setConditionNameMap(Map<String, String> conditionNameMap) {
        this.conditionNameMap = conditionNameMap;
    }

    public Class getModelClass() {
        return modelClass;
    }

    public void setModelClass(Class modelClass) {
        this.modelClass = modelClass;
    }
}