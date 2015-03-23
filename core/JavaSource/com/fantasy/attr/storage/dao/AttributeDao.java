package com.fantasy.attr.storage.dao;

import org.springframework.stereotype.Repository;

import com.fantasy.attr.storage.bean.Attribute;
import com.fantasy.framework.dao.hibernate.HibernateDao;

@Repository
public class AttributeDao  extends HibernateDao<Attribute, Long> {

}
