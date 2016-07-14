package org.jfantasy.pay.dao;

import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.pay.bean.Card;
import org.springframework.stereotype.Repository;

@Repository
public class CardDao extends HibernateDao<Card,String>{
}
