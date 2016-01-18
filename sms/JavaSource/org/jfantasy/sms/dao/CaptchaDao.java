package org.jfantasy.sms.dao;

import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.sms.bean.Captcha;
import org.springframework.stereotype.Repository;

@Repository
public class CaptchaDao extends HibernateDao<Captcha, String> {

}
