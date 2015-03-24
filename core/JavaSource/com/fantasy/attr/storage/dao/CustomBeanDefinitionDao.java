package com.fantasy.attr.storage.dao;

import com.fantasy.attr.storage.bean.CustomBeanDefinition;
import com.fantasy.framework.dao.hibernate.HibernateDao;
import org.springframework.stereotype.Repository;

@Repository
public class CustomBeanDefinitionDao extends HibernateDao<CustomBeanDefinition,Long>{
}
