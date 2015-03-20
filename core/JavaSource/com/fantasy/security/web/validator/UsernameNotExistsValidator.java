package com.fantasy.security.web.validator;

import com.fantasy.framework.struts2.validator.validators.AjaxValidatorSupport;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.security.service.UserService;
import com.opensymphony.xwork2.validator.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;

public class UsernameNotExistsValidator extends AjaxValidatorSupport {

	@Autowired
	private UserService userService;

	public UsernameNotExistsValidator() {
	}

	public void validate(Object object) throws ValidationException {
		String fieldName = super.getFieldName();
		String username = StringUtil.nullValue(getFieldValue(fieldName, object));
		String userId = StringUtil.nullValue(getFieldValue("user.id", object));
		if (!this.userService.usernameNotExists(username, StringUtil.isNotBlank(userId) ? Long.valueOf(userId) : null)){
            addFieldError(fieldName, object);
        }

	}
}