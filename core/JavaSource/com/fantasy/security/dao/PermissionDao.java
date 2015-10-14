package com.fantasy.security.dao;

import com.fantasy.framework.dao.hibernate.HibernateDao;
import com.fantasy.security.bean.Permission;
import org.springframework.stereotype.Repository;

@Repository
public class PermissionDao extends HibernateDao<Permission, Long> {
}
