package org.jfantasy.events;

import org.jfantasy.security.bean.User;
import org.springframework.context.ApplicationEvent;

public class RegisterEvent extends ApplicationEvent {

    public RegisterEvent(User user) {
        super(user);
    }

}
