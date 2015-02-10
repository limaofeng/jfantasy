package com.fantasy.security.dao;

import com.fantasy.framework.dao.hibernate.HibernateDao;
import com.fantasy.security.bean.Organization;
import org.springframework.stereotype.Repository;

/**
 * Created by hebo on 2015/1/6.
 *
 */
@Repository
public class OrganizationDao extends HibernateDao<Organization,String> {
}
