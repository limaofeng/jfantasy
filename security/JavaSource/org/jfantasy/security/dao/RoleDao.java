package org.jfantasy.security.dao;

import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.security.bean.Role;
import org.springframework.stereotype.Repository;

@Repository("fantasy.auth.hibernate.RoleDao")
public class RoleDao extends HibernateDao<Role, String> {

}