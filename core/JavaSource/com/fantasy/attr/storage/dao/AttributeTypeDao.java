package com.fantasy.attr.storage.dao;

import org.springframework.stereotype.Repository;

import com.fantasy.attr.storage.bean.AttributeType;
import com.fantasy.framework.dao.hibernate.HibernateDao;

@Repository
public class AttributeTypeDao extends HibernateDao<AttributeType, Long> {

}
