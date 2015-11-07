package com.fantasy.security.event.context;


import com.fantasy.security.bean.User;
import org.springframework.context.ApplicationEvent;

public class LogoutEvent extends ApplicationEvent {

    public LogoutEvent(User user) {
        super(user);
    }

}