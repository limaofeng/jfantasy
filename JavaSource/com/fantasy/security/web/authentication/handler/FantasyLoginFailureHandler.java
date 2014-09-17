package com.fantasy.security.web.authentication.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.ExceptionMappingAuthenticationFailureHandler;

import com.fantasy.framework.util.common.ObjectUtil;

/**
 * 登陆失败后的操作
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2013-6-28 下午04:47:46
 * @version 1.0
 */
public class FantasyLoginFailureHandler extends ExceptionMappingAuthenticationFailureHandler implements InitializingBean {

	private List<AuthenticationFailureHandler> handlers = null;

	public void afterPropertiesSet() throws Exception {
		if (ObjectUtil.isNull(handlers)) {
			handlers = new ArrayList<AuthenticationFailureHandler>();
		}
	}

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
		for (AuthenticationFailureHandler handler : handlers) {
			handler.onAuthenticationFailure(request, response, exception);
		}
		super.onAuthenticationFailure(request, response, exception);
	}
	
	public void setHandlers(List<AuthenticationFailureHandler> handlers) {
		this.handlers = handlers;
	}

}
