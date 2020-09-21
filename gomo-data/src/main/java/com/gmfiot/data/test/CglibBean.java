package com.gmfiot.data.test;

import org.springframework.cglib.beans.BeanGenerator;
import org.springframework.cglib.beans.BeanMap;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class CglibBean {

    public Object object = null;
    public BeanMap beanMap = null;

    public CglibBean(){
        super();
    }

    public CglibBean(Map propertyMap){
        this.object = generateBean(propertyMap);
        this.beanMap = BeanMap.create(this.object);
    }

    public void setValue(String property, Object value) {
        beanMap.put(property, value);
    }

    public Object getValue(String property) {
        return beanMap.get(property);
    }

    public Object getObject() {
        return this.object;
    }

    private Object generateBean(Map propertyMap){
        BeanGenerator generator = new BeanGenerator();
        Set keySet = propertyMap.keySet();
        for (Iterator i = keySet.iterator(); i.hasNext();){
            String key = (String) i.next();
            generator.addProperty(key,(Class)propertyMap.get(key));
        }
        return generator.create();
    }
}
