package com.fantasy.mall.delivery.dao;

import org.springframework.stereotype.Repository;

import com.fantasy.framework.dao.hibernate.HibernateDao;

import com.fantasy.mall.delivery.bean.DeliveryCorp;

@Repository("mall.dao.DeliveryCorpDao")
public class DeliveryCorpDao extends HibernateDao<DeliveryCorp, Long> {
	

}
