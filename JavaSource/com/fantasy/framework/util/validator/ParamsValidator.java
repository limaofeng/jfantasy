package com.fantasy.framework.util.validator;

import com.fantasy.framework.util.validator.exception.ValidationException;
import java.util.Map;

public abstract interface ParamsValidator extends Validator {
	public abstract void validate(Object paramObject, Map<String, String> paramMap) throws ValidationException;

	public abstract void setParams(Map<String, String> paramMap);

	public abstract Map<String, String> getParams();
}