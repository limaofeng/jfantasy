package org.jfantasy.framework.util.ognl.typeConverter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target( { java.lang.annotation.ElementType.PARAMETER, java.lang.annotation.ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface DateFormat {

	public abstract String pattern();

}