package org.jfantasy.member.listener;

import org.jfantasy.member.bean.Member;
import org.jfantasy.member.bean.TeamMember;
import org.jfantasy.member.event.TeamInviteEvent;
import org.jfantasy.member.service.MemberService;
import org.jfantasy.member.service.TeamMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 团队要求监听
 */
@Component
public class TeamInviteListener implements ApplicationListener<TeamInviteEvent> {

    @Autowired
    private TeamMemberService teamMemberService;
    @Autowired
    private MemberService memberService;

    @Override
    @Async
    @Transactional
    public void onApplicationEvent(TeamInviteEvent event) {
        Member member = memberService.findUniqueByUsername(event.getMobile());
        TeamMember teamMember = teamMemberService.findUnique(event.getMobile(), event.getMobile());
        if (member != null && teamMember != null) {
            teamMember.setMember(member);
            teamMemberService.update(teamMember, true);
        }
    }

}
