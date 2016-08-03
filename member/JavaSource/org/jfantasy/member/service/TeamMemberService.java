package org.jfantasy.member.service;

import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.member.bean.TeamMember;
import org.jfantasy.member.bean.enums.InviteStatus;
import org.jfantasy.member.dao.TeamMemberDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TeamMemberService {

    @Autowired
    private TeamMemberDao teamMemberDao;

    public Pager<TeamMember> findPager(Pager<TeamMember> pager, List<PropertyFilter> filters) {
        return teamMemberDao.findPager(pager, filters);
    }

    public TeamMember get(Long id) {
        return this.teamMemberDao.get(id);
    }

    public void deltele(Long... ids) {
        this.teamMemberDao.delete(ids);
    }

    @Transactional
    public TeamMember update(TeamMember member) {
        return this.teamMemberDao.update(member, true);
    }

    @Transactional
    public TeamMember save(TeamMember member) {
        member.setStatus(InviteStatus.unactivated);
        return this.teamMemberDao.save(member);
    }
}
