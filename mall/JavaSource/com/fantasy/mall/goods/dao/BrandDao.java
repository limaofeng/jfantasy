package com.fantasy.mall.goods.dao;

import org.springframework.stereotype.Repository;
import com.fantasy.framework.dao.hibernate.HibernateDao;
import com.fantasy.mall.goods.bean.Brand;

@Repository
public class BrandDao extends HibernateDao<Brand, Long> {

}
