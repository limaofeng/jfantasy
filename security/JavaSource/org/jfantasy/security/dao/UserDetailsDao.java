package org.jfantasy.security.dao;

import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.security.bean.UserDetails;
import org.springframework.stereotype.Repository;

@Repository("fantasy.auth.hibernate.UserDetailsDao")
public class UserDetailsDao extends HibernateDao<UserDetails, Long> {

}
