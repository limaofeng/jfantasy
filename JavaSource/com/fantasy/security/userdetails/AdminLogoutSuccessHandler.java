package com.fantasy.security.userdetails;

import com.fantasy.security.service.UserService;
import com.fantasy.security.web.authentication.handler.FantasyLogoutSuccessHandler;
import com.fantasy.system.service.WebAccessLogService;
import org.springframework.security.core.Authentication;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AdminLogoutSuccessHandler extends FantasyLogoutSuccessHandler{

	@Resource(name="fantasy.auth.UserService")
	private UserService userService;
	@Resource
	private WebAccessLogService accessLogService;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
	}
	
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		super.onLogoutSuccess(request, response, authentication);
		if (authentication != null) {
			AdminUser details = (AdminUser) authentication.getPrincipal();
			userService.logout(details.getUser());
			accessLogService.log(request, details);
		}
	}

}
