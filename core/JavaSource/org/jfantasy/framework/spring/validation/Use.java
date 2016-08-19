package org.jfantasy.framework.spring.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = UseConstraintValidator.class)
@Target({METHOD,FIELD})
@Retention(RUNTIME)
@Documented
public @interface Use {

    String message() default "自定义错误";

    Class<? extends Validator> vali();

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}