package org.jfantasy.security.dao;

import org.springframework.stereotype.Repository;

import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.security.bean.Role;

@Repository("fantasy.auth.hibernate.RoleDao")
public class RoleDao extends HibernateDao<Role, String> {

}