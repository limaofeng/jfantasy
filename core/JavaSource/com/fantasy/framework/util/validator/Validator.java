package com.fantasy.framework.util.validator;

import com.fantasy.framework.util.validator.exception.ValidationException;

public abstract interface Validator {
	public abstract void validate(Object paramObject) throws ValidationException;
}