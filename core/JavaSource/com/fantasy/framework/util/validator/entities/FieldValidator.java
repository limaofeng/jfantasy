package com.fantasy.framework.util.validator.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fantasy.framework.util.validator.ParamsValidator;
import com.fantasy.framework.util.validator.Validateable;
import com.fantasy.framework.util.validator.Validator;
import com.fantasy.framework.util.validator.exception.Error;
import com.fantasy.framework.util.validator.exception.StackValidationException;
import com.fantasy.framework.util.validator.exception.ValidationException;

public class FieldValidator {
	private String type;
	private Map<String, String> params;

	public FieldValidator(String type, Map<String, String> params) {
		this.type = type;
		this.params = params;
	}

	public String getType() {
		return this.type;
	}

	public Map<String, String> getParams() {
		return this.params;
	}

	public Error[] validate(Validateable validateable, Object value) {
		Validator validator = validateable.getValidator(this.type);
		try {
			if (validator == null) {
				throw new RuntimeException(this.type + ",对应的验证器没有找到!");
			}
			if ((validator instanceof ParamsValidator)) {
				ParamsValidator paramsValidator = (ParamsValidator) ParamsValidator.class.cast(validator);
				if (paramsValidator.getParams() != null) {
					for (Map.Entry<String, String> entry : paramsValidator.getParams().entrySet()) {
						String key = (String) entry.getKey();
						String val = (String) entry.getValue();
						if (!this.params.containsKey(key)) {
							this.params.put(key, val);
						}
					}
				}
				paramsValidator.validate(value, this.params);
			} else {
				validator.validate(value);
			}
		} catch (StackValidationException e) {
			return e.getStack();
		} catch (ValidationException e) {
			if (this.params.containsKey("message")) {
				return new Error[] { new Error(((String) this.params.get("message")).toString()) };
			}
			return new Error[] { new Error(e.getMessage()) };
		}
		return null;
	}

	@Deprecated
	public Error[] validate(Validateable validateable, List<?> values) {
		List<Error> allError = new ArrayList<Error>();
		for (Iterator<?> localIterator = values.iterator(); localIterator.hasNext();) {
			Object value = localIterator.next();
			Error[] errors = validate(validateable, value);
			for (Error error : errors) {
				System.out.println("验证项:" + error.getField() + ",错误信息:" + error.getMessage());
			}
			allError.addAll(Arrays.asList(errors));
		}
		return (Error[]) allError.toArray(new Error[allError.size()]);
	}
}