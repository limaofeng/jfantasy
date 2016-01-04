package org.jfantasy.attr.storage.dao;

import org.jfantasy.attr.storage.bean.CustomBean;
import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.springframework.stereotype.Repository;

@Repository
public class CustomBeanDao extends HibernateDao<CustomBean,Long> {

}
