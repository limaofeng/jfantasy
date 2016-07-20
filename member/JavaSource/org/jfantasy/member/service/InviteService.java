package org.jfantasy.member.service;

import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.member.bean.Invite;
import org.jfantasy.member.bean.Team;
import org.jfantasy.member.dao.InviteDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InviteService {

    @Autowired
    private InviteDao inviteDao;

    public Pager<Invite> findPager(Pager<Invite> pager, List<PropertyFilter> filters) {
        return this.inviteDao.findPager(pager, filters);
    }

    @Transactional
    public void deltele(Long... ids) {
        for (Long id : ids) {
            this.inviteDao.delete(id);
        }
    }

    public Invite get(Long id) {
        return this.inviteDao.get(id);
    }

    @Transactional
    public List<Invite> save(String teamid, List<Invite> invites) {
        for (int i = 0, size = invites.size(); i < size; i++) {
            Invite invite = invites.get(i);
            invite.setTeam(new Team(teamid));
            invites.set(i, this.save(invite));
        }
        return invites;
    }

    @Transactional
    public Invite save(Invite invite) {
        return this.inviteDao.save(invite);
    }

}
