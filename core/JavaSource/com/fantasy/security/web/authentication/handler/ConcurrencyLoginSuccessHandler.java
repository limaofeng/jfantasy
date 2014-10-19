package com.fantasy.security.web.authentication.handler;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.fantasy.framework.util.common.ObjectUtil;

public class ConcurrencyLoginSuccessHandler implements AuthenticationSuccessHandler {

	private SessionRegistry sessionRegistry;
	private int maxSessions = 1;

	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		List<SessionInformation> sessions = sessionRegistry.getAllSessions(authentication.getPrincipal(), false);
		if (ObjectUtil.indexOf(sessions, "sessionId", request.getSession().getId()) == -1) {
			sessionRegistry.registerNewSession(request.getSession().getId(), authentication.getPrincipal());
		} else {
			ObjectUtil.remove(sessions, "sessionId", request.getSession().getId());
		}
		while(sessions.size() > (maxSessions-1)){
			allowableSessionsExceeded(sessions);
		}
	}
	
	private void allowableSessionsExceeded(List<SessionInformation> sessions) {
		SessionInformation leastRecentlyUsed = null;
		for (SessionInformation session : sessions) {
			if ((leastRecentlyUsed == null) || session.getLastRequest().before(leastRecentlyUsed.getLastRequest())) {
				leastRecentlyUsed = session;
			}
		}
		leastRecentlyUsed.expireNow();
		ObjectUtil.remove(sessions, "sessionId", leastRecentlyUsed.getSessionId());
	}

	public void setSessionRegistry(SessionRegistry sessionRegistry) {
		this.sessionRegistry = sessionRegistry;
	}

	public void setMaxSessions(int maxSessions) {
		this.maxSessions = maxSessions;
	}

}
