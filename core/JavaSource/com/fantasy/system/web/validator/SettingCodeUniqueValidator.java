package com.fantasy.system.web.validator;

import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.system.service.SettingService;
import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.validators.FieldValidatorSupport;

import javax.annotation.Resource;


public class SettingCodeUniqueValidator extends FieldValidatorSupport {

	@Autowired
	private SettingService settingService;

	@Override
	public void validate(Object obj) throws ValidationException {
		String fieldName = getFieldName();
		String id = StringUtil.nullValue(getFieldValue("id", obj));
		String key = StringUtil.nullValue(getFieldValue(fieldName, obj));
        Long websiteId =Long.valueOf(StringUtil.nullValue(getFieldValue("website.id", obj)));
		if (!settingService.settingCodeUnique(key,websiteId, StringUtil.isBlank(id) ? null : Long.valueOf(id))) {
			addFieldError(fieldName, obj);
		}
	}

}
