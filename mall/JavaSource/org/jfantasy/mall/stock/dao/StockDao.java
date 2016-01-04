package org.jfantasy.mall.stock.dao;

import org.springframework.stereotype.Repository;

import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.mall.stock.bean.Stock;

/**
 *@Author lsz
 *@Date 2013-11-28 下午4:49:22
 *
 */
@Repository
public class StockDao extends HibernateDao<Stock,Long> {
	
	
}

