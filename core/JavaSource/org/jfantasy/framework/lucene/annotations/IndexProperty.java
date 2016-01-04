package org.jfantasy.framework.lucene.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标注需要索引的字段<br/>
 * 支持的数据类型包括：String、char、boolean、int、long、float、double、Date等基本数据类型。<br/>
 * 还支持上述基本数据类型组成的数组、List、Set等。这些集合中的元素，不管是什么数据类型，都会连结成一个字符串，然后加以索引。
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-1-23 下午03:01:02
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IndexProperty {
    /**
     * boolean型，表示是否需要分词，缺省值为false
     *
     * @return boolean
     */
    boolean analyze() default false;

    /**
     * boolean型，表示是否需要存储，缺省值为 false
     *
     * @return boolean
     */
    boolean store() default false;

    /**
     * float型，表示该Field的权重，缺省值为1.0
     *
     * @return boolean
     */
    float boost() default 1.0f;
}
