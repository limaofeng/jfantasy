package org.jfantasy.framework.util.validator.validators;

import org.jfantasy.framework.util.validator.Validator;
import org.jfantasy.framework.util.validator.exception.ValidationException;

import java.util.List;

public class ArrayValidator implements Validator {
    public void validate(Object object) throws ValidationException {
        if (!(object instanceof List<?>)) {
            throw new ValidationException("对象不是数组.");
        }
    }
}