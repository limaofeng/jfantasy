package org.jfantasy.member.service;

import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.member.bean.Team;
import org.jfantasy.member.dao.TeamDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TeamService {

    @Autowired
    private TeamDao teamDao;

    public Pager<Team> findPager(Pager<Team> pager, List<PropertyFilter> filters) {
        return teamDao.findPager(pager, filters);
    }

    @Transactional
    public Team save(Team team) {
        return this.teamDao.save(team);
    }

    @Transactional
    public Team update(Team team) {
        this.teamDao.update(team);
        return team;
    }

    @Transactional
    public void deltele(String... ids) {
        for (String id : ids) {
            this.teamDao.delete(id);
        }
    }

    public Team get(String id) {
        return this.teamDao.get(id);
    }

}
