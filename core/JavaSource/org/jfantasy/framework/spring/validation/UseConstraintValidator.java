package org.jfantasy.framework.spring.validation;

import org.jfantasy.framework.spring.SpringContextUtil;
import org.jfantasy.framework.util.common.ClassUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UseConstraintValidator implements ConstraintValidator<Use, String> {

    private Validator<String> validator;

    @Override
    public void initialize(Use use) {
        Class<? extends Validator> clazz = use.vali();
        validator = SpringContextUtil.getBeanByType(clazz);
        if (validator == null) {
            validator = ClassUtil.newInstance(clazz);
        }
    }

    @Override
    public boolean isValid(String str, ConstraintValidatorContext constraintValidatorContext) {
        try {
            validator.validate(str);
        } catch (ValidationException e) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate(e.getMessage()).addConstraintViolation();
            return false;
        }
        return true;
    }

}
