package org.jfantasy.security.context;

import org.jfantasy.security.bean.User;
import org.springframework.context.ApplicationEvent;

public class LoginEvent extends ApplicationEvent {

    public LoginEvent(User user) {
        super(user);
    }

    public User getUser() {
        return (User)this.getSource();
    }
}
