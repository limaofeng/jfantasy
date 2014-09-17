package com.fantasy.common.dao;

import com.fantasy.common.bean.JdbcConfig;
import com.fantasy.framework.dao.hibernate.HibernateDao;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcConfigDao extends HibernateDao<JdbcConfig, Long> {

}
