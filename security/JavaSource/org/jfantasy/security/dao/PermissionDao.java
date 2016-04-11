package org.jfantasy.security.dao;

import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.security.bean.Permission;
import org.springframework.stereotype.Repository;

@Repository
public class PermissionDao extends HibernateDao<Permission, Long> {
}
