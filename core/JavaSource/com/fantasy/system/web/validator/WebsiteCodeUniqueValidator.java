package com.fantasy.system.web.validator;

import javax.annotation.Resource;

import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.system.service.WebsiteService;
import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.validators.FieldValidatorSupport;


public class WebsiteCodeUniqueValidator extends FieldValidatorSupport {

	@Autowired
	private WebsiteService websiteService;

	@Override
	public void validate(Object obj) throws ValidationException {
		String fieldName = getFieldName();
		String id = StringUtil.nullValue(getFieldValue("id", obj));
		String key = StringUtil.nullValue(getFieldValue(fieldName, obj));
		if (!websiteService.websiteCodeUnique(key, StringUtil.isBlank(id) ? null : Long.valueOf(id))) {
			addFieldError(fieldName, obj);
		}
	}

}
