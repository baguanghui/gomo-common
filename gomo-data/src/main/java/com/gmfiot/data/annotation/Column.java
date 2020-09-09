package com.gmfiot.data.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
    String name() default "fieldName";
    boolean unique() default false;
    boolean nullable() default false;
    int length() default 0;
    boolean updatable() default true;
}
