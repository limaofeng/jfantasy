package org.jfantasy.pay.dao;

import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.pay.bean.Order;
import org.jfantasy.pay.order.entity.OrderKey;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDao extends HibernateDao<Order,OrderKey> {

}
