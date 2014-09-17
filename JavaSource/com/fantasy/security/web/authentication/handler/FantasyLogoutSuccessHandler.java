package com.fantasy.security.web.authentication.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import com.fantasy.framework.util.common.ObjectUtil;

/**
 * 成功登出后的操作
 * 
 * @author 李茂峰
 * 
 */
public class FantasyLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler implements InitializingBean {

	private List<LogoutSuccessHandler> handlers = null;

	public void afterPropertiesSet() throws Exception {
		if (ObjectUtil.isNull(handlers)) {
			handlers = new ArrayList<LogoutSuccessHandler>();
		}
	}

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		for (LogoutSuccessHandler handler : handlers) {
			handler.onLogoutSuccess(request, response, authentication);
		}
		super.onLogoutSuccess(request, response, authentication);
	}

	public void setHandlers(List<LogoutSuccessHandler> handlers) {
		this.handlers = handlers;
	}
	
	
}
