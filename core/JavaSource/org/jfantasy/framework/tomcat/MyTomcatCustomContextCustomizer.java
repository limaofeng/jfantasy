package org.jfantasy.framework.tomcat;

import org.apache.catalina.Context;
import org.apache.catalina.Manager;
import org.apache.catalina.Session;
import org.apache.catalina.session.StandardManager;
import org.apache.catalina.session.StandardSession;
import org.springframework.boot.context.embedded.tomcat.TomcatContextCustomizer;

import java.io.IOException;

public class MyTomcatCustomContextCustomizer implements TomcatContextCustomizer {

    @Override
    public void customize(Context context) {
        context.setManager(new NoSessionManager());
        context.setCookies(false);
    }

    public static class NoSessionManager extends StandardManager {

        NoSession noSession = new NoSession(this);

        @Override
        public Session createSession(String sessionId) {
            return noSession;
        }

        @Override
        public Session findSession(String id) throws IOException {
            return noSession;
        }
    }

    public static class NoSession extends StandardSession {

        public NoSession(Manager manager) {
            super(manager);
        }

        @Override
        public String getId() {
            return super.getId();
        }

        @Override
        public boolean isValid() {
            return true;
        }

        public void setAttribute(String arg0, Object arg1) {
        }

        @Override
        public Object getAttribute(String name) {
            return null;
        }
    }

}
