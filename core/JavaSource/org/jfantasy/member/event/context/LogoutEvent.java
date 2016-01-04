package org.jfantasy.member.event.context;

import org.jfantasy.member.bean.Member;
import org.springframework.context.ApplicationEvent;

public class LogoutEvent extends ApplicationEvent {

    public LogoutEvent(Member member) {
        super(member);
    }

}
