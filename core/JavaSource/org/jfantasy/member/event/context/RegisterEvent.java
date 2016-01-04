package org.jfantasy.member.event.context;

import org.jfantasy.member.bean.Member;
import org.springframework.context.ApplicationEvent;

public class RegisterEvent extends ApplicationEvent {

    public RegisterEvent(Member member) {
        super(member);
    }

}
