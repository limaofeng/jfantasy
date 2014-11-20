package com.fantasy.framework.ws.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * DTO mapping 注解,用于对象转换或者propertyFilter自动转换时，匹配对应的业务bean字段
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Mapping {

    public abstract String value() default "";
}
