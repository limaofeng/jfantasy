package org.jfantasy.member.event;

import org.springframework.context.ApplicationEvent;

public class TeamInviteEvent extends ApplicationEvent {

    public TeamInviteEvent(String teamId, String mobile) {
        super(new Object());
    }

    public String getTeamId(){
        return null;
    }

    public String getMobile(){
        return null;
    }

}
