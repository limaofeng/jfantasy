package com.fantasy.security.userdetails.checker;

import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.framework.util.web.context.ActionContext;
import com.octo.captcha.service.CaptchaService;
import com.octo.captcha.service.CaptchaServiceException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 李茂峰
 * @version 1.0
 * @功能描述
 * @since 2013-9-13 上午10:32:02
 */
public class CaptchaChecker implements UserDetailsChecker {

    private static final Log LOGGER = LogFactory.getLog(CaptchaChecker.class);

    private CaptchaService captchaService;
    private String captchaParameter = "captcha";
    private String exceptionMessage = "验证码输入错误";

    public void check(UserDetails toCheck) {
        if (!validateCaptchaChallenge(ActionContext.getContext().getHttpRequest())) {
            throw new AuthenticationServiceException(exceptionMessage);
        }
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
            if (StringUtil.isBlank(challengeResponse)) {
                return false;
            }
            return this.captchaService.validateResponseForID(captchaID, challengeResponse.toUpperCase()).booleanValue();
        } catch (CaptchaServiceException e) {
            LOGGER.error(e.getMessage(), e);
            return false;
        }
    }

    public void setCaptchaService(CaptchaService captchaService) {
        this.captchaService = captchaService;
    }

    public void setCaptchaParameter(String captchaParameter) {
        this.captchaParameter = captchaParameter;
    }

    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

}
