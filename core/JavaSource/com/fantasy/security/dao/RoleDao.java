package com.fantasy.security.dao;

import org.springframework.stereotype.Repository;

import com.fantasy.framework.dao.hibernate.HibernateDao;
import com.fantasy.security.bean.Role;

@Repository("fantasy.auth.hibernate.RoleDao")
public class RoleDao extends HibernateDao<Role, String> {
	
}