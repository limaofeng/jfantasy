package org.jfantasy.member.dao.listener;

import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostUpdateEvent;
import org.jfantasy.framework.dao.annotations.EventListener;
import org.jfantasy.framework.dao.hibernate.listener.AbstractChangedListener;
import org.jfantasy.member.bean.TeamMember;
import org.jfantasy.member.event.TeamInviteEvent;
import org.springframework.stereotype.Component;

/**
 * 当团队添加用户时,发送邀请
 */
@Component
@EventListener(type = "post-commit-insert")
public class TeamMemberInsertListener extends AbstractChangedListener<TeamMember> {

    private static final long serialVersionUID = 4221243459220184177L;

    @Override
    public void onPostInsert(TeamMember member, PostInsertEvent event) {
        getApplicationContext().publishEvent(new TeamInviteEvent(member.getTeamId(), member.getMobile()));
    }

    @Override
    public void onPostUpdate(TeamMember member, PostUpdateEvent event) {
    }

}
