package com.fantasy.security.web.authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fantasy.framework.util.common.StringUtil;
import com.octo.captcha.service.CaptchaService;
import com.octo.captcha.service.CaptchaServiceException;

public class CaptchaUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private CaptchaService captchaService;
	private String captchaParameter = "captcha";
	private String errorMessage = "验证码错误";

	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		if (!validateCaptchaChallenge(request)) {
			throw new AuthenticationServiceException(errorMessage);
		}
		return super.attemptAuthentication(request, response);
	}

	/**
	 * 验证验证码
	 * 
	 * @param request
	 * @return
	 */
	protected boolean validateCaptchaChallenge(HttpServletRequest request) {
		try {
			String captchaID = request.getSession().getId();
			String challengeResponse = request.getParameter(this.captchaParameter);
            return StringUtil.isBlank(challengeResponse) ? false : this.captchaService.validateResponseForID(captchaID, challengeResponse.toUpperCase());
        } catch (CaptchaServiceException e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public void setCaptchaParameter(String captchaParameter) {
		this.captchaParameter = captchaParameter;
	}

	public void setCaptchaService(CaptchaService captchaService) {
		this.captchaService = captchaService;
	}
}