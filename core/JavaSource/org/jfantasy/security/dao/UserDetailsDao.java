package org.jfantasy.security.dao;

import org.springframework.stereotype.Repository;

import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.security.bean.UserDetails;

@Repository("fantasy.auth.hibernate.UserDetailsDao")
public class UserDetailsDao extends HibernateDao<UserDetails, Long> {

}
