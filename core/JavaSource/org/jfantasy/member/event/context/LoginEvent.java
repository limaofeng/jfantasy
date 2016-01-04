package org.jfantasy.member.event.context;

import org.jfantasy.member.bean.Member;
import org.springframework.context.ApplicationEvent;

public class LoginEvent extends ApplicationEvent {

    public LoginEvent(Member member) {
        super(member);
    }

}
