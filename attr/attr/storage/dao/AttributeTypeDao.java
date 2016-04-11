package org.jfantasy.attr.storage.dao;

import org.jfantasy.attr.storage.bean.AttributeType;
import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.springframework.stereotype.Repository;

@Repository
public class AttributeTypeDao extends HibernateDao<AttributeType, Long> {

}
