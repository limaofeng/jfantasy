package com.fantasy.cms.web.validator;

import javax.annotation.Resource;

import com.fantasy.cms.service.CmsService;
import com.fantasy.framework.util.common.StringUtil;
import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.validators.FieldValidatorSupport;


public class CmsActicleCategoryCodeUniqueValidator extends FieldValidatorSupport {

	@Resource
	private CmsService cmsService;

	@Override
	public void validate(Object obj) throws ValidationException {
		String fieldName = getFieldName();
		String code = StringUtil.nullValue(getFieldValue(fieldName, obj));
		if (!cmsService.articleCategoryUnique(code)) {
			addFieldError(fieldName, obj);
		}
	}

}
