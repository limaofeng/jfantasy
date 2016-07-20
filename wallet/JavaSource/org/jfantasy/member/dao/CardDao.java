package org.jfantasy.member.dao;

import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.member.bean.Card;
import org.springframework.stereotype.Repository;

@Repository("mem.cardDao")
public class CardDao extends HibernateDao<Card,Long> {
}
