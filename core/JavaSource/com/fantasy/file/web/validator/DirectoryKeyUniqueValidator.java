package com.fantasy.file.web.validator;

import javax.annotation.Resource;

import com.fantasy.file.service.DirectoryService;
import com.fantasy.framework.util.common.StringUtil;
import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.validators.FieldValidatorSupport;

public class DirectoryKeyUniqueValidator extends FieldValidatorSupport {

	@Autowired
	private DirectoryService directoryService;
	
	@Override
	public void validate(Object obj) throws ValidationException {
		String fieldName = getFieldName();
		String key = StringUtil.nullValue(getFieldValue(fieldName, obj));
		if (!directoryService.direcroryKeyUnique(key)) {
			addFieldError(fieldName, obj);
		}
		
	}
}
