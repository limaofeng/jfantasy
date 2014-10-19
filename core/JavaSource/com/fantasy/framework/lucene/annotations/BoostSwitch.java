package com.fantasy.framework.lucene.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 同一个Entity类的不同的Document，可能需要设置不同的权重<br/>
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2013-1-23 下午03:15:53
 * @version 1.0
 */
@Target( { java.lang.annotation.ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface BoostSwitch {
	/**
	 * 与@IndexFilter的compare含义相同
	 * 
	 * @功能描述
	 * @return
	 */
	public Compare compare();

	/**
	 * 与@IndexFilter的value含义相同
	 * 
	 * @功能描述
	 * @return
	 */
	public String value();

	/**
	 * float型，表示满足比较条件时该Document的boost值，缺省值为1.0
	 * 
	 * @功能描述
	 * @return
	 */
	public float fit() default 1.0f;

	/**
	 * 表示不满足比较条件时该Document的boost值，缺省值为1.0
	 * 
	 * @功能描述
	 * @return
	 */
	public float unfit() default 1.0f;
}