package com.gmfiot.core.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;

import java.util.*;

/**
 * @author BaGuangHui
 */
public class JsonUtil {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.setTimeZone(TimeZone.getDefault());
        OBJECT_MAPPER.setPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CAMEL_CASE);
        OBJECT_MAPPER.setLocale(Locale.CHINESE);
        OBJECT_MAPPER.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
        OBJECT_MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,false);
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
    }

    public static String toJson(Object data) {
        if(data == null){
            return null;
        }
        try {
            String string = OBJECT_MAPPER.writeValueAsString(data);
            return string;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T toObject(String jsonData, Class<?> clazz) {
        if(StringUtil.isBlank(jsonData)){
            return null;
        }
        try {
            T t = (T)OBJECT_MAPPER.readValue(jsonData, clazz);
            return t;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T toObject(String jsonData, Class<?> clazz, Class<?> parametricClass) {
        if(parametricClass == null){
            return toObject(jsonData,clazz);
        }
        if(StringUtil.isBlank(jsonData)){
            return null;
        }
        JavaType javaType = OBJECT_MAPPER.getTypeFactory().constructParametricType(clazz, parametricClass);
        try {
            T t =(T)OBJECT_MAPPER.readValue(jsonData, javaType);
            return t;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
