package com.fantasy.framework.struts2.validator.validators;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fantasy.framework.struts2.validator.AjaxValidatorAction;
import com.fantasy.framework.util.common.ClassUtil;
import com.fantasy.framework.util.common.ObjectUtil;
import com.opensymphony.xwork2.util.ValueStack;
import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.validators.FieldValidatorSupport;

public abstract class AjaxValidatorSupport extends FieldValidatorSupport {

	public Map<String, List<String>> getFieldErrors() {
		return getValidatorContext().getFieldErrors();
	}

	public List<String> getFieldError(String fieldName) {
		return getFieldErrors().get(fieldName);
	}

	public List<String> getFieldError() {
		return ObjectUtil.defaultValue(getFieldErrors().get(getFieldName()), new ArrayList<String>());
	}

	protected void addFieldError(String propertyName, Object object) {
		super.addFieldError(propertyName, object);
	}

	@SuppressWarnings("unchecked")
	public Object getFieldValue(String fieldName, Object object) throws ValidationException {
		if (object instanceof AjaxValidatorAction) {
			ValueStack stack = (ValueStack) ClassUtil.getValue(this, "stack");
			Map<String, Object> parameters = (Map<String, Object>) stack.findValue("#parameters");
			Object array = parameters.get(fieldName);
			if ((ClassUtil.isArray(array)) && Array.getLength(array) == 1) {
				return Array.get(array, 0).toString();
			} else {
				return array;
			}
		} else {
			return super.getFieldValue(fieldName, object);
		}
	}

}