package com.fantasy.member.userdetails;

import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.member.service.MemberService;
import com.fantasy.security.web.authentication.handler.ConcurrencyLoginSuccessHandler;
import com.fantasy.security.web.authentication.handler.FantasyLoginSuccessHandler;
import com.fantasy.system.service.WebAccessLogService;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import org.springframework.beans.factory.annotation.Autowired;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MemberLoginSuccessHandler extends FantasyLoginSuccessHandler {

	@Autowired
	private MemberService memberService;
	@Autowired
	private WebAccessLogService accessLogService;

	@Override
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
        AuthenticationSuccessHandler handler = SpringContextUtil.getBeanByType(ConcurrencyLoginSuccessHandler.class);
        if (handler == null) {
            handler = SpringContextUtil.createBean(ConcurrencyLoginSuccessHandler.class, SpringContextUtil.AutoType.AUTOWIRE_BY_TYPE);
        }
        this.handlers.add(handler);
	}

	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		MemberUser details = (MemberUser) authentication.getPrincipal();
		super.onAuthenticationSuccess(request, response, authentication);
		memberService.login(details.getUser());
		accessLogService.log(request, details);
	}

}
