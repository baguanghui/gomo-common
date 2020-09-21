package com.gmfiot.data;

import com.gmfiot.core.util.ReflectionUtil;

import java.util.HashMap;
import java.util.Map;

public class QueryMap extends HashMap<String,Object> {
    public QueryMap(Object query){
        var queryMap = ReflectionUtil.getNotNullFieldValueMap(query);
        putAll(queryMap);
    }
}
