package org.jfantasy.attr.storage.dao;

import org.jfantasy.attr.storage.bean.AttributeVersion;
import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.springframework.stereotype.Repository;

@Repository
public class AttributeVersionDao extends HibernateDao<AttributeVersion,Long>{
}
