package com.fantasy.framework.lucene.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表示需要嵌入到其它对象的@Ref或@RefList域的索引中。
 *
 * @author 李茂峰
 * @version 1.0
 * @功能描述
 * @since 2013-1-23 下午03:07:36
 */
@Target({java.lang.annotation.ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IndexRefBy {
    /**
     * value——Class类型，表示被引用的类
     *
     * @return
     * @功能描述
     */
    public abstract Class<?>[] value();

    /**
     * analyze——boolean型，表示是否需要分词
     *
     * @return
     * @功能描述
     */
    public abstract boolean[] analyze() default false;

    /**
     * store——boolean型，表示是否需要存储
     *
     * @return
     * @功能描述
     */
    public abstract boolean[] store() default true;

    /**
     * boost——float型，表示该Field的权重
     *
     * @return
     * @功能描述
     */
    public abstract float[] boost() default 1.0f;
}