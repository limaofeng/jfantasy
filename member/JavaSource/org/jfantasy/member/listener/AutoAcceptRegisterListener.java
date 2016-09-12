package org.jfantasy.member.listener;

import org.hibernate.criterion.Restrictions;
import org.jfantasy.member.bean.Member;
import org.jfantasy.member.bean.TeamMember;
import org.jfantasy.member.bean.enums.TeamMemberStatus;
import org.jfantasy.member.event.RegisterEvent;
import org.jfantasy.member.service.TeamMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class AutoAcceptRegisterListener implements ApplicationListener<RegisterEvent> {

    @Autowired
    private TeamMemberService teamMemberService;

    @Override
    public void onApplicationEvent(RegisterEvent event) {
        Member member = event.getMember();
        for(TeamMember teamMember : teamMemberService.find(Restrictions.eq("mobile",member.getUsername()),Restrictions.eq("status",TeamMemberStatus.unactivated))){
            teamMember.setMemberId(member.getId());
            teamMember.setStatus(TeamMemberStatus.activated);
            teamMemberService.update(teamMember, true);
        }
    }

}
