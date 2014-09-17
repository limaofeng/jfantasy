package com.fantasy.attr.dao;

import org.springframework.stereotype.Repository;

import com.fantasy.attr.bean.AttributeType;
import com.fantasy.framework.dao.hibernate.HibernateDao;

@Repository
public class AttributeTypeDao extends HibernateDao<AttributeType, Long> {

}
