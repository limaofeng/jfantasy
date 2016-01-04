package org.jfantasy.mall.goods.dao;

import org.springframework.stereotype.Repository;
import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.mall.goods.bean.Brand;

@Repository
public class BrandDao extends HibernateDao<Brand, Long> {

}
