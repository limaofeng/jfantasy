package com.fantasy.member.event.context;

import com.fantasy.member.bean.Member;
import org.springframework.context.ApplicationEvent;

public class LoginEvent extends ApplicationEvent {

    public LoginEvent(Member member) {
        super(member);
    }

}
