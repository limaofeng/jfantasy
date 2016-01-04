package org.jfantasy.common.dao;

import org.jfantasy.common.bean.JdbcConfig;
import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcConfigDao extends HibernateDao<JdbcConfig, Long> {

}
