package org.jfantasy.security.dao;

import org.springframework.stereotype.Repository;

import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.security.bean.User;

@Repository("fantasy.auth.hibernate.UserDao")
public class UserDao extends HibernateDao<User, Long> {

}