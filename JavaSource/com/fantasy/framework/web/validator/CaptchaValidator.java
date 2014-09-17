package com.fantasy.framework.web.validator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.fantasy.framework.struts2.validator.AjaxValidatorAction;
import com.fantasy.framework.struts2.validator.validators.AjaxValidatorSupport;
import com.fantasy.framework.util.common.ClassUtil;
import com.fantasy.framework.util.common.StringUtil;
import com.octo.captcha.service.CaptchaService;
import com.octo.captcha.service.CaptchaServiceException;
import com.octo.captcha.service.captchastore.CaptchaStore;
import com.opensymphony.xwork2.validator.ValidationException;

public class CaptchaValidator extends AjaxValidatorSupport {

	private static final Log logger = LogFactory.getLog(CaptchaValidator.class);

	private CaptchaService captchaService;
	private CaptchaStore captchaStore;

	public CaptchaValidator() {
	}

	public void setCaptchaService(CaptchaService captchaService) {
		this.captchaService = captchaService;
		this.captchaStore = (CaptchaStore) ClassUtil.getValue(captchaService, "store");
	}

	public void validate(Object object) throws ValidationException {
		String fieldName = getFieldName();
		String id = ServletActionContext.getRequest().getSession().getId();
		String code = ServletActionContext.getRequest().getParameter(fieldName);
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("SESSIONID:" + id + "\t输入验证码:" + code);
			}
			if (object instanceof AjaxValidatorAction) {
				if (!captchaStore.hasCaptcha(id)) {
					addFieldError(fieldName, object);
				}else if (!captchaStore.getCaptcha(id).validateResponse(StringUtil.nullValue(code).toUpperCase())) {
					addFieldError(fieldName, object);
				}
			} else if (!this.captchaService.validateResponseForID(id, StringUtil.nullValue(code).toUpperCase())) {
				addFieldError(fieldName, object);
			}
		} catch (CaptchaServiceException e) {
			logger.error(e.getMessage());
			addFieldError(fieldName, object);
		}
	}

}
