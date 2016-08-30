package org.jfantasy.member.event;

import org.springframework.context.ApplicationEvent;

public class TeamInviteEvent extends ApplicationEvent {

    public TeamInviteEvent(String teamId, String mobile) {
        super(new TeamInviteSource(teamId,mobile));
    }

    public String getTeamId(){
        return ((TeamInviteSource)this.getSource()).teamId;
    }

    public String getMobile(){
        return ((TeamInviteSource)this.getSource()).mobile;
    }

    private static class TeamInviteSource{
        private String teamId;
        private String mobile;

        private TeamInviteSource(String teamId, String mobile) {
            this.teamId = teamId;
            this.mobile = mobile;
        }

    }

}
