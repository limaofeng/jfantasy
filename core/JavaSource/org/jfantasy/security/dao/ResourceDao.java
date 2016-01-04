package org.jfantasy.security.dao;

import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.security.bean.Resource;
import org.springframework.stereotype.Repository;

@Repository("fantasy.auth.hibernate.ResourceDao")
public class ResourceDao extends HibernateDao<Resource, Long> {

}