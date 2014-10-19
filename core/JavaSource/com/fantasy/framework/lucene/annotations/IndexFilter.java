package com.fantasy.framework.lucene.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表示只有满足该条件的实体才会被索引，否则不创建索引。<br/>
 * IndexFilter有2个参数：compare和value。<br/>
 * compare表示比较操作，是枚举类型Compare。value是比较的值，是字符串，会相应的解析成该属性类型的值。 <br/>
 * 在一个Entity类上可以有多个@IndexFilter注解，表示需要同时满足这些条件。
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2013-1-23 下午03:11:13
 * @version 1.0
 */
@Target( { java.lang.annotation.ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface IndexFilter {

	public Compare compare();

	public String value();
}