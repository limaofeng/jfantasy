package org.jfantasy.security.event.context;

import org.jfantasy.security.bean.User;
import org.springframework.context.ApplicationEvent;

public class LoginEvent extends ApplicationEvent {

    public LoginEvent(User user) {
        super(user);
    }

}
