package org.jfantasy.security.context;


import org.jfantasy.security.bean.User;
import org.springframework.context.ApplicationEvent;

public class LogoutEvent extends ApplicationEvent {

    public LogoutEvent(User user) {
        super(user);
    }

}