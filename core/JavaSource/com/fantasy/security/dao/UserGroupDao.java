package com.fantasy.security.dao;

import org.springframework.stereotype.Repository;

import com.fantasy.framework.dao.hibernate.HibernateDao;
import com.fantasy.security.bean.UserGroup;

@Repository("fantasy.auth.hiberante.UserGroupDao")
public class UserGroupDao extends HibernateDao<UserGroup, Long> {
}