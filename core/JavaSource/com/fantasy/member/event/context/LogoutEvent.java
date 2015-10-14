package com.fantasy.member.event.context;

import com.fantasy.member.bean.Member;
import org.springframework.context.ApplicationEvent;

public class LogoutEvent extends ApplicationEvent {

    public LogoutEvent(Member member) {
        super(member);
    }

}
