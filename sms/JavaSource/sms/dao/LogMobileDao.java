package org.jfantasy.sms.dao;

import org.springframework.stereotype.Repository;

import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.sms.bean.LogMobile;

@Repository
public class LogMobileDao extends HibernateDao<LogMobile, Long> {

}
