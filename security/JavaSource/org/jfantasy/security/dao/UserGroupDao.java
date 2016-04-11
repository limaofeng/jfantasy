package org.jfantasy.security.dao;

import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.security.bean.UserGroup;
import org.springframework.stereotype.Repository;

@Repository("fantasy.auth.hiberante.UserGroupDao")
public class UserGroupDao extends HibernateDao<UserGroup, Long> {
}