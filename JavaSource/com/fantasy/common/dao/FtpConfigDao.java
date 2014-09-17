package com.fantasy.common.dao;

import org.springframework.stereotype.Repository;

import com.fantasy.common.bean.FtpConfig;
import com.fantasy.framework.dao.hibernate.HibernateDao;

@Repository
public class FtpConfigDao extends HibernateDao<FtpConfig, Long> {

}
