package org.jfantasy.security.dao;

import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.security.bean.Job;
import org.springframework.stereotype.Repository;

/**
 * Created by yhx on 2015/2/4.
 */
@Repository
public class JobDao extends HibernateDao<Job,Long> {
}
