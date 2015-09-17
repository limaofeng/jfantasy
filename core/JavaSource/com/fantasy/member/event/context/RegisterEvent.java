package com.fantasy.member.event.context;

import com.fantasy.member.bean.Member;
import org.springframework.context.ApplicationEvent;

public class RegisterEvent extends ApplicationEvent {

    public RegisterEvent(Member member) {
        super(member);
    }

}
