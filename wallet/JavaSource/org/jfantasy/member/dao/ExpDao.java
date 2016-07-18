package org.jfantasy.member.dao;

import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.member.bean.Exp;
import org.springframework.stereotype.Repository;

@Repository
public class ExpDao extends HibernateDao<Exp, Long> {
}
