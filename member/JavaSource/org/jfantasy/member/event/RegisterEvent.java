package org.jfantasy.member.event;

import org.jfantasy.member.bean.Member;
import org.springframework.context.ApplicationEvent;

public class RegisterEvent extends ApplicationEvent {

    private static final long serialVersionUID = 8813504517382299661L;

    public RegisterEvent(Member member) {
        super(member);
    }

    public Member getMember(){
        return (Member)this.getSource();
    }

}
