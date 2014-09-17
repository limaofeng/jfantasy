package com.fantasy.attr.dao;

import org.springframework.stereotype.Repository;

import com.fantasy.attr.bean.Attribute;
import com.fantasy.framework.dao.hibernate.HibernateDao;

@Repository
public class AttributeDao  extends HibernateDao<Attribute, Long> {

}
