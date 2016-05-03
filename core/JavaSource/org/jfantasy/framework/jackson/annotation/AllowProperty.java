package org.jfantasy.framework.jackson.annotation;

import java.lang.annotation.*;

/**
 * 只允许pojo内的属性序列化成json，对于同一个pojo该注解是与IgnoreProperty是冲突的<br>
 */
@Documented
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AllowProperty {
    /**
     * 目标POJO <br>
     * 2013-9-27 下午4:27:08
     *
     * @return pojos
     */
    Class<?> pojo();

    /**
     * 允许序列化的属性名 <br>
     * 2013-9-27 下午4:27:12
     *
     * @return names
     */
    String[] name();
}