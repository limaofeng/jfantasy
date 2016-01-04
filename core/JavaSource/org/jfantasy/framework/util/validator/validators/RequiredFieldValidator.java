package org.jfantasy.framework.util.validator.validators;

import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.framework.util.validator.Validator;
import org.jfantasy.framework.util.validator.exception.ValidationException;

public class RequiredFieldValidator implements Validator {
    public void validate(Object object) throws ValidationException {
        if ((ObjectUtil.isNull(object)) || (StringUtil.isNull(object.toString()))) {
            throw new ValidationException("");
        }
    }
}