package org.jfantasy.common.dao;

import org.springframework.stereotype.Repository;

import org.jfantasy.common.bean.FtpConfig;
import org.jfantasy.framework.dao.hibernate.HibernateDao;

@Repository
public class FtpConfigDao extends HibernateDao<FtpConfig, Long> {

}
