package org.jfantasy.member.dao;

import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.member.bean.Team;
import org.springframework.stereotype.Repository;

@Repository
public class TeamDao extends HibernateDao<Team,String>{
}
