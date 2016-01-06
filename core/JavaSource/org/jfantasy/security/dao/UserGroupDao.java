package org.jfantasy.security.dao;

import org.springframework.stereotype.Repository;

import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.security.bean.UserGroup;

@Repository("fantasy.auth.hiberante.UserGroupDao")
public class UserGroupDao extends HibernateDao<UserGroup, Long> {
}