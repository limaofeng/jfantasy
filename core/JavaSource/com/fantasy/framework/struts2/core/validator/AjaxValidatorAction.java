package com.fantasy.framework.struts2.core.validator;

import com.fantasy.framework.struts2.ActionSupport;
import com.fantasy.framework.struts2.core.validator.validators.AjaxValidatorSupport;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.inject.Inject;
import com.opensymphony.xwork2.validator.DelegatingValidatorContext;
import com.opensymphony.xwork2.validator.Validator;
import com.opensymphony.xwork2.validator.ValidatorConfig;
import com.opensymphony.xwork2.validator.ValidatorFactory;

public class AjaxValidatorAction extends ActionSupport {
	private static final long serialVersionUID = -3762677919656498904L;
	private transient ValidatorFactory validatorFactory;

	@Inject
	public void setValidatorFactory(ValidatorFactory validatorFactory) {
		this.validatorFactory = validatorFactory;
	}

	@SuppressWarnings("rawtypes")
	public String ajaxValidator(String fieldName, String validatorType) throws Exception {
		Validator validator = this.validatorFactory.getValidator(new ValidatorConfig.Builder(validatorType).removeParam("methodName").build());
		if ((validator instanceof AjaxValidatorSupport)) {
			AjaxValidatorSupport afvalidator = (AjaxValidatorSupport) validator;
			afvalidator.setValidatorContext(new DelegatingValidatorContext(this));
			afvalidator.setFieldName(fieldName);
			afvalidator.setDefaultMessage("ajax validator error!");
			afvalidator.setValueStack(ActionContext.getContext().getValueStack());
			afvalidator.validate(this);
			if(this.hasErrors()){
				this.addActionError("ajax validator error!");
			}
		}
		return SUCCESS;
	}

}