package org.jfantasy.framework.lucene.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表示需要映射到MongoDB中的一个实体。<br/>
 * 如果设置了capped=true，则需要设置capSize和capMax两者中的其中一个。
 *
 * @author 李茂峰
 * @version 1.0
 * @功能描述
 * @since 2013-1-23 下午03:22:48
 */
@Target({java.lang.annotation.ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Entity {
    /**
     * String型，表示其在MongoDB中的collection的名称。name属性可以省略，默认使用类名的全小写。
     *
     * @return
     * @功能描述
     */
    public String name();

    /**
     * boolean型，表示该Entity类对应的是Capped Collection，缺省值为false。
     *
     * @return
     * @功能描述
     */
    public boolean capped();

    /**
     * long型，设置Capped Collection的空间大小，以字节为单位，默认值为-1，表示未设置。
     *
     * @return
     * @功能描述
     */
    public long capSize();

    /**
     * long型，设置Capped Collection的最多能存储多少个document，默认值为-1，表示未设置。
     *
     * @return
     * @功能描述
     */
    public long capMax();
}