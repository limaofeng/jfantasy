package com.fantasy.framework.util.validator.validators;

import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.framework.util.validator.Validator;
import com.fantasy.framework.util.validator.exception.ValidationException;

public class RequiredFieldValidator implements Validator {
	public void validate(Object object) throws ValidationException {
		if ((ObjectUtil.isNull(object)) || (StringUtil.isNull(object.toString()))){
            throw new ValidationException("");
        }
	}
}