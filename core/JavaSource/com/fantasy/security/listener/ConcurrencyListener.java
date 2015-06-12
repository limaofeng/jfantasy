package com.fantasy.security.listener;

import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.framework.util.web.context.ActionContext;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class ConcurrencyListener implements ApplicationListener<AuthenticationSuccessEvent> {

    private SessionRegistry sessionRegistry;
    private int maxSessions = 1;

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        HttpServletRequest request = ActionContext.getContext().getHttpRequest();
        HttpServletResponse response = ActionContext.getContext().getHttpResponse();
        List<SessionInformation> sessions = sessionRegistry.getAllSessions(event.getAuthentication().getPrincipal(), false);

        if (ObjectUtil.indexOf(sessions, "sessionId", request.getSession().getId()) == -1) {
            sessionRegistry.registerNewSession(request.getSession().getId(), event.getAuthentication().getPrincipal());
        } else {
            ObjectUtil.remove(sessions, "sessionId", request.getSession().getId());
        }
        while (sessions.size() > (maxSessions - 1)) {
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
        assert leastRecentlyUsed != null;
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
