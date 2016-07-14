package org.jfantasy.pay.dao;

import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.pay.bean.CardDesign;
import org.springframework.stereotype.Repository;

@Repository
public class CardDesignDao extends HibernateDao<CardDesign, String> {
}
