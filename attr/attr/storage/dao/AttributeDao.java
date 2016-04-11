package org.jfantasy.attr.storage.dao;

import org.jfantasy.attr.storage.bean.Attribute;
import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.springframework.stereotype.Repository;

@Repository
public class AttributeDao  extends HibernateDao<Attribute, Long> {

}
