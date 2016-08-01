package org.jfantasy.member.dao;

import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.member.bean.TeamMember;
import org.springframework.stereotype.Repository;

@Repository
public class TeamMemberDao extends HibernateDao<TeamMember, Long> {
}
