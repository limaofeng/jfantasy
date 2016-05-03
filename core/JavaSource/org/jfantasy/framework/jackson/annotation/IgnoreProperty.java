package org.jfantasy.framework.jackson.annotation;


import java.lang.annotation.*;

/**
 * 用于注解json过滤pojo内的属性，其他的属性都会被序列化成字符串
 */
@Documented
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface IgnoreProperty {
    /**
     * 要忽略字段的POJO <br>
     * 2013-9-27 下午4:27:08
     *
     * @return pojos
     */
    Class<?> pojo();

    /**
     * 要忽略的字段名 <br>
     * 2013-9-27 下午4:27:12
     *
     * @return names
     */
    String[] name();

}