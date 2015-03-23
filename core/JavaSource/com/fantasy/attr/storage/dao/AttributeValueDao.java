package com.fantasy.attr.storage.dao;

import com.fantasy.attr.storage.bean.AttributeValue;
import com.fantasy.framework.dao.hibernate.HibernateDao;
import org.springframework.stereotype.Repository;

@Repository
public class AttributeValueDao extends HibernateDao<AttributeValue, Long> {
}
