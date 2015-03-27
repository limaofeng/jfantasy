package com.fantasy.attr.storage.dao;

import com.fantasy.attr.storage.bean.CustomBean;
import com.fantasy.framework.dao.hibernate.HibernateDao;
import org.springframework.stereotype.Repository;

@Repository
public class CustomBeanDao extends HibernateDao<CustomBean,Long> {

}
