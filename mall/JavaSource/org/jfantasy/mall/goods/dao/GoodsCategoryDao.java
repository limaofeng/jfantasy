package org.jfantasy.mall.goods.dao;

import org.springframework.stereotype.Repository;

import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.mall.goods.bean.GoodsCategory;

@Repository
public class GoodsCategoryDao extends HibernateDao<GoodsCategory, Long> {

}