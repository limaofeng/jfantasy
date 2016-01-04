package org.jfantasy.attr.storage.dao;

import org.springframework.stereotype.Repository;

import org.jfantasy.attr.storage.bean.Attribute;
import org.jfantasy.framework.dao.hibernate.HibernateDao;

@Repository
public class AttributeDao  extends HibernateDao<Attribute, Long> {

}
