package org.jfantasy.attr.storage.dao;

import org.jfantasy.attr.storage.bean.CustomBeanDefinition;
import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.springframework.stereotype.Repository;

@Repository
public class CustomBeanDefinitionDao extends HibernateDao<CustomBeanDefinition,Long>{
}
