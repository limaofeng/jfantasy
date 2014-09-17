package com.fantasy.security.dao;

import org.springframework.stereotype.Repository;

import com.fantasy.framework.dao.hibernate.HibernateDao;
import com.fantasy.security.bean.UserDetails;

@Repository("fantasy.auth.hibernate.UserDetailsDao")
public class UserDetailsDao extends HibernateDao<UserDetails, Long> {

}
