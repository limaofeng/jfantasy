package com.fantasy.security.web.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.filter.GenericFilterBean;

import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.security.SpringSecurityUtils;

/**
 * 自动以默认用户名登录的filter, 用于开发时不需要每次进入登录页面.s
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2013-4-9 上午10:11:14
 * @version 1.0
 */
public class AutoLoginFilter extends GenericFilterBean {

	private boolean enabled = false;

	private String defaultUserName;

	private String userDetailsServiceBeanName;

	private String successHandlersBeanName;

	private UserDetailsService userDetailsService;

	private List<AuthenticationSuccessHandler> successHandlers;

	@SuppressWarnings("unchecked")
	@Override
	protected void initFilterBean() throws ServletException {
		if (StringUtil.isBlank(userDetailsServiceBeanName)) {
			userDetailsService = SpringContextUtil.getBeanByType(UserDetailsService.class);
		} else {
			userDetailsService = SpringContextUtil.getBean(userDetailsServiceBeanName, UserDetailsService.class);
		}
		if (StringUtil.isBlank(successHandlersBeanName)) {
			successHandlers = new ArrayList<AuthenticationSuccessHandler>();
		} else {
			successHandlers = ((List<AuthenticationSuccessHandler>) SpringContextUtil.getBean(successHandlersBeanName));
		}
		if (this.logger.isDebugEnabled()) {
			StringBuffer buffer = new StringBuffer("\r\n自动登陆配置信息:");
			buffer.append("\r\nenabled:" + enabled);
			buffer.append("\r\ndefaultUserName:" + defaultUserName);
			buffer.append("\r\nuserDetailsServiceBeanName:" + userDetailsServiceBeanName);
			buffer.append("\r\nuserDetailsService:" + userDetailsService);
			logger.debug(buffer);
		}
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpSession session = ((HttpServletRequest) request).getSession();
		String requestURL = ((HttpServletRequest) request).getRequestURL().toString();

		if (enabled && session.getAttribute("SPRING_SECURITY_CONTEXT") == null) {
			UserDetails userDetails = userDetailsService.loadUserByUsername(defaultUserName);
			if (this.logger.isDebugEnabled()) {
				logger.debug("执行自动登陆:" + defaultUserName);
			}
			if (userDetails == null) {
				if (this.logger.isDebugEnabled()) {
					logger.debug("自动登陆失败[" + defaultUserName + "]未找到");
				}
				chain.doFilter(request, response);
			} else {// 执行登陆操作
				logger.debug("用户[" + defaultUserName + "]自动登陆成功");
				SpringSecurityUtils.saveUserDetailsToContext(userDetails, (HttpServletRequest) request);
				for (AuthenticationSuccessHandler handler : successHandlers) {
					handler.onAuthenticationSuccess((HttpServletRequest) request, (HttpServletResponse) response, SecurityContextHolder.getContext().getAuthentication());
				}
				session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
				((HttpServletResponse) response).sendRedirect(requestURL);
			}
		} else {
			chain.doFilter(request, response);
		}
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Required
	public void setDefaultUserName(String defaultUserName) {
		this.defaultUserName = defaultUserName;
	}

	public void setUserDetailsServiceBeanName(String userDetailsServiceBeanName) {
		this.userDetailsServiceBeanName = userDetailsServiceBeanName;
	}

	public void setSuccessHandlersBeanName(String successHandlersBeanName) {
		this.successHandlersBeanName = successHandlersBeanName;
	}

}
