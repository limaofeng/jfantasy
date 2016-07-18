package org.jfantasy.member.dao;

import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.member.bean.Level;
import org.springframework.stereotype.Repository;

@Repository("mem.LevelDao")
public class LevelDao extends HibernateDao<Level, Long> {
}
