package com.gmfiot.data.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {
    /**
     * 指定表名，不知道时表名为类名复数
     * @return
     */
    String name() default "className";

    /**
     * 指定数据库名
     * @return
     */
    String schema() default "";
}
