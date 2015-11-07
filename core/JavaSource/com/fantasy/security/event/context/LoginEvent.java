package com.fantasy.security.event.context;

import com.fantasy.security.bean.User;
import org.springframework.context.ApplicationEvent;

public class LoginEvent extends ApplicationEvent {

    public LoginEvent(User user) {
        super(user);
    }

}
