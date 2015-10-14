package com.fantasy.events;

import com.fantasy.security.bean.User;
import org.springframework.context.ApplicationEvent;

public class RegisterEvent extends ApplicationEvent {

    public RegisterEvent(User user) {
        super(user);
    }

}
