package com.gmfiot.data;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class QueryInfo {
    private String typeName;
    private List<String> properties;
    private Map<String, Method> getMethodMap;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public List<String> getProperties() {
        return properties;
    }

    public void setProperties(List<String> properties) {
        this.properties = properties;
    }

    public Map<String, Method> getGetMethodMap() {
        return getMethodMap;
    }

    public void setGetMethodMap(Map<String, Method> getMethodMap) {
        this.getMethodMap = getMethodMap;
    }
}