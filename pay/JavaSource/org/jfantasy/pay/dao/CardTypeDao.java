package org.jfantasy.pay.dao;

import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.pay.bean.CardType;
import org.springframework.stereotype.Repository;

@Repository
public class CardTypeDao extends HibernateDao<CardType, String> {
}
