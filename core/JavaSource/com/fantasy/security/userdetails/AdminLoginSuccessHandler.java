package com.fantasy.security.userdetails;

import com.fantasy.security.service.UserService;
import com.fantasy.security.web.authentication.handler.FantasyLoginSuccessHandler;
import com.fantasy.system.service.WebAccessLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AdminLoginSuccessHandler extends FantasyLoginSuccessHandler{

	@Autowired
	private UserService userService;
	@Autowired
	private WebAccessLogService accessLogService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		super.onAuthenticationSuccess(request, response, authentication);
		AdminUser details = (AdminUser)authentication.getPrincipal();
		userService.login(details.getUser());
		accessLogService.log(request, details);
	}

}
