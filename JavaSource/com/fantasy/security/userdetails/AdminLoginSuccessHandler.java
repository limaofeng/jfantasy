package com.fantasy.security.userdetails;

import com.fantasy.contacts.service.handler.AddressBookLoginSuccessHandler;
import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.security.service.UserService;
import com.fantasy.security.web.authentication.handler.ConcurrencyLoginSuccessHandler;
import com.fantasy.security.web.authentication.handler.FantasyLoginSuccessHandler;
import com.fantasy.system.service.WebAccessLogService;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AdminLoginSuccessHandler extends FantasyLoginSuccessHandler{

	@Resource(name="fantasy.auth.UserService")
	private UserService userService;
	@Resource
	private WebAccessLogService accessLogService;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
		AuthenticationSuccessHandler handler = SpringContextUtil.getBeanByType(AddressBookLoginSuccessHandler.class);
		if (handler == null) {
			handler = SpringContextUtil.createBean(AddressBookLoginSuccessHandler.class, SpringContextUtil.AUTOWIRE_BY_TYPE);
		}
		this.handlers.add(handler);
		handler = SpringContextUtil.getBeanByType(ConcurrencyLoginSuccessHandler.class);
		if (handler == null) {
			handler = SpringContextUtil.createBean(ConcurrencyLoginSuccessHandler.class, SpringContextUtil.AUTOWIRE_BY_TYPE);
		}
		this.handlers.add(handler);
	}
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		super.onAuthenticationSuccess(request, response, authentication);
		AdminUser details = (AdminUser)authentication.getPrincipal();
		userService.login(details.getUser());
		accessLogService.log(request, details);
	}

}
