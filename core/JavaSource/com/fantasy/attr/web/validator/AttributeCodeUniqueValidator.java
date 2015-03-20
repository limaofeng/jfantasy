package com.fantasy.attr.web.validator;

import org.springframework.beans.factory.annotation.Autowired;

import com.fantasy.attr.storage.service.AttributeService;
import com.fantasy.framework.util.common.StringUtil;
import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.validators.FieldValidatorSupport;

public class AttributeCodeUniqueValidator extends FieldValidatorSupport {

	@Autowired
	private AttributeService attributeService;

	@Override
	public void validate(Object obj) throws ValidationException {
		String fieldName = getFieldName();
		String id = StringUtil.nullValue(getFieldValue("id", obj));
		String code = StringUtil.nullValue(getFieldValue(fieldName, obj));
		if (!attributeService.attributeCodeUnique(code, StringUtil.isBlank(id) ? null : Long.valueOf(id))) {
			addFieldError(fieldName, obj);
		}
	}

}
