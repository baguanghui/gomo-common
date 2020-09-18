package com.gmfiot.data.sql;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * @author BaGuangHui
 */
public class ColumnInfo {
    private String name;
    private Boolean unique;
    private Boolean nullable;
    private Integer length;
    private Boolean updatable;
    private Method readMethod;
    private Method writeMethod;
    private String typeName;

    /**
     * 获取字段值
     * @param model
     * @return
     */
    public Object getValue(Object model){
        try {
            return readMethod.invoke(model);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 设置字段值
     * @param model
     * @param value
     */
    public void setValue(Object model,Object value){
        try {
            writeMethod.invoke(model,value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getUnique() {
        return unique;
    }

    public void setUnique(Boolean unique) {
        this.unique = unique;
    }

    public Boolean getNullable() {
        return nullable;
    }

    public void setNullable(Boolean nullable) {
        this.nullable = nullable;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Boolean getUpdatable() {
        return updatable;
    }

    public void setUpdatable(Boolean updatable) {
        this.updatable = updatable;
    }

    public void setReadMethod(Method readMethod) {
        this.readMethod = readMethod;
    }

    public void setWriteMethod(Method writeMethod) {
        this.writeMethod = writeMethod;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
