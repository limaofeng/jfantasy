package org.jfantasy.sms.dao;

import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.sms.bean.CaptchaConfig;
import org.springframework.stereotype.Repository;

@Repository
public class CaptchaConfigDao extends HibernateDao<CaptchaConfig, String>{

}
