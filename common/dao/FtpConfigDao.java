package org.jfantasy.common.dao;

import org.jfantasy.common.bean.FtpConfig;
import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.springframework.stereotype.Repository;

@Repository
public class FtpConfigDao extends HibernateDao<FtpConfig, Long> {

}
