package com.fantasy.mall.goods.dao;

import org.springframework.stereotype.Repository;

import com.fantasy.framework.dao.hibernate.HibernateDao;
import com.fantasy.mall.goods.bean.GoodsCategory;

@Repository
public class GoodsCategoryDao extends HibernateDao<GoodsCategory, Long> {

}