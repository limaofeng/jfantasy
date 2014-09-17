package com.fantasy.framework.util.validator.validators;

import com.fantasy.framework.util.validator.Validator;
import com.fantasy.framework.util.validator.exception.ValidationException;
import java.util.List;

public class ArrayValidator implements Validator {
	public void validate(Object object) throws ValidationException {
		if (!(object instanceof List<?>))
			throw new ValidationException("对象不是数组.");
	}
}