package com.fantasy.framework.jcaptcha;

import com.octo.captcha.service.CaptchaServiceException;
import com.octo.captcha.service.image.DefaultManageableImageCaptchaService;

/**
 * 通过读取配置，判断是否验证验证码。
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2013-8-5 下午03:01:10
 * @version 1.0
 */
public class FantasyCaptchaService extends DefaultManageableImageCaptchaService {

	@Override
	public Boolean validateResponseForID(String iD, Object response) throws CaptchaServiceException {
		return super.validateResponseForID(iD, response);
	}

}
