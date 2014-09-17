package com.fantasy.framework.log.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.fantasy.framework.log.filter.DefaultLogFilter;

@Target( { ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Log {
	/**
	 * 简要描述信息
	 * 
	 * @功能描述
	 * @return
	 */
	public abstract String text() default "";

	/**
	 * 判断是否记录日志
	 * 
	 * @功能描述
	 * @return
	 */
	public abstract String condition() default "";

	/**
	 * 日志连接器类型
	 * 
	 * @功能描述
	 * @return
	 */
	public abstract String type() default "simple";

	/**
	 * 日志拦截器
	 * 
	 * @功能描述
	 * @return
	 */
	public abstract Class<?> using() default DefaultLogFilter.class;
}
