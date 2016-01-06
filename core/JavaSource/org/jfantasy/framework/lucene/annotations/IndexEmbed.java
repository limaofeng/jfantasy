package org.jfantasy.framework.lucene.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表示需要嵌入对该Embed对象的索引。结合@IndexEmbedBy使用。索引域的名称形如“embed.x”。
 *
 * @author 李茂峰
 * @version 1.0
 * @功能描述
 * @since 2013-1-23 下午03:03:13
 */
@Target({java.lang.annotation.ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IndexEmbed {

}