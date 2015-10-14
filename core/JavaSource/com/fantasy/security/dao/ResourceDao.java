package com.fantasy.security.dao;

import com.fantasy.framework.dao.hibernate.HibernateDao;
import com.fantasy.security.bean.Resource;
import org.springframework.stereotype.Repository;

@Repository("fantasy.auth.hibernate.ResourceDao")
public class ResourceDao extends HibernateDao<Resource, Long> {

}