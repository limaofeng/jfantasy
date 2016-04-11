package org.jfantasy.security.web.authentication.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import org.jfantasy.framework.util.common.ObjectUtil;

/**
 * 登陆成功后的操作
 *
 * @author 李茂峰
 * @version 1.0
 * @功能描述
 * @since 2013-6-28 下午04:48:09
 */
public class FantasyLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler implements InitializingBean {

    protected List<AuthenticationSuccessHandler> handlers = null;

    public void afterPropertiesSet() throws Exception {
        if (ObjectUtil.isNull(handlers)) {
            handlers = new ArrayList<AuthenticationSuccessHandler>();
        }
    }

    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        for (AuthenticationSuccessHandler handler : handlers) {
            handler.onAuthenticationSuccess(request, response, authentication);
        }
        super.onAuthenticationSuccess(request, response, authentication);
    }

    public void setHandlers(List<AuthenticationSuccessHandler> handlers) {
        this.handlers = handlers;
    }

}
