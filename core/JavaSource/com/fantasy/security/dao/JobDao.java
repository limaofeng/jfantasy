package com.fantasy.security.dao;

import com.fantasy.framework.dao.hibernate.HibernateDao;
import com.fantasy.security.bean.Job;
import org.springframework.stereotype.Repository;

/**
 * Created by yhx on 2015/2/4.
 */
@Repository
public class JobDao extends HibernateDao<Job,Long> {
}
