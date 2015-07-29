package com.fantasy.security.dao;

import org.springframework.stereotype.Repository;

import com.fantasy.framework.dao.hibernate.HibernateDao;
import com.fantasy.security.bean.User;

@Repository("fantasy.auth.hibernate.UserDao")
public class UserDao extends HibernateDao<User, Long> {

}