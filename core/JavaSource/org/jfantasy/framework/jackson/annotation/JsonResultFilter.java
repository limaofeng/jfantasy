package org.jfantasy.framework.jackson.annotation;


import java.lang.annotation.*;

/**
 * json属性过滤注解，对于同一个pojo来说 @AllowProperty 是与 @IgnoreProperty 是冲突的，如果这两个注解都注解了取 AllowProperty
 */
@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonResultFilter {

    /**
     * 要过滤的属性
     *
     * @return ignorePropertys
     */
    IgnoreProperty[] ignore() default {};

    /**
     * 允许的属性
     *
     * @return allowPropertys
     */
    AllowProperty[] allow() default {};

    /**
     * 当返回 pager 对象时,该参数有效。设置为 list 时只返回 pager.pageItems 内容。
     * @return class
     */
    Class value() default Object.class;
}
