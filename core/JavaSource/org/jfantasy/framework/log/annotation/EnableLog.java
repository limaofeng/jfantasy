package org.jfantasy.framework.log.annotation;

import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.context.annotation.AdviceMode;

import java.lang.annotation.*;

import static org.springframework.context.annotation.AdviceMode.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(LogConfigurationSelector.class)
public @interface EnableLog {

    boolean proxyTargetClass() default false;

    AdviceMode mode() default PROXY;

    int order() default Ordered.LOWEST_PRECEDENCE;

}
