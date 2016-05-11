package org.jfantasy.pay.dao;


import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.pay.bean.OrderServer;
import org.springframework.stereotype.Repository;

@Repository
public class OrderServerDao extends HibernateDao<OrderServer, String> {
}
