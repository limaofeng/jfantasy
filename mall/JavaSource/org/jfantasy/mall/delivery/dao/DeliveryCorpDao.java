package org.jfantasy.mall.delivery.dao;

import org.springframework.stereotype.Repository;

import org.jfantasy.framework.dao.hibernate.HibernateDao;

import org.jfantasy.mall.delivery.bean.DeliveryCorp;

@Repository("mall.dao.DeliveryCorpDao")
public class DeliveryCorpDao extends HibernateDao<DeliveryCorp, Long> {
	

}
