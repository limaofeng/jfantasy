package com.fantasy.member.userdetails;

import com.fantasy.member.service.MemberService;
import com.fantasy.security.web.authentication.handler.FantasyLogoutSuccessHandler;
import com.fantasy.system.service.WebAccessLogService;
import org.springframework.security.core.Authentication;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MemberLogOutSuccessHandler extends FantasyLogoutSuccessHandler {

	@Autowired
	private MemberService memberService;
	@Autowired
	private WebAccessLogService accessLogService;

	@Override
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
	}

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		super.onLogoutSuccess(request, response, authentication);
		if (authentication != null) {
			MemberUser details = (MemberUser) authentication.getPrincipal();
			memberService.logout(details.getUser());
			accessLogService.log(request, details);
		}
	}

}
