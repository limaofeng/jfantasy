package org.jfantasy.sms.dao;

import org.springframework.stereotype.Repository;

import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.sms.bean.CaptchaConfig;

@Repository
public class CaptchaConfigDao extends HibernateDao<CaptchaConfig, String>{

}
