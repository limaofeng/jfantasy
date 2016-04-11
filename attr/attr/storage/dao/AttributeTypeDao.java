package org.jfantasy.attr.storage.dao;

import org.springframework.stereotype.Repository;

import org.jfantasy.attr.storage.bean.AttributeType;
import org.jfantasy.framework.dao.hibernate.HibernateDao;

@Repository
public class AttributeTypeDao extends HibernateDao<AttributeType, Long> {

}
