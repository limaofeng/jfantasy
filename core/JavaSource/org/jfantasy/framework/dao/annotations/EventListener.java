package org.jfantasy.framework.dao.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({java.lang.annotation.ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface EventListener {

    String[] type() default {"post-commit-insert", "post-commit-update"};

}
