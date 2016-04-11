package org.jfantasy.events;

import org.jfantasy.framework.util.json.bean.User;
import org.springframework.context.ApplicationEvent;

public class RegisterEvent extends ApplicationEvent {

    public RegisterEvent(User user) {
        super(user);
    }

}
