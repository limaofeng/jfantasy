package org.jfantasy.sms.dao;

import org.springframework.stereotype.Repository;

import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.sms.bean.Captcha;

@Repository
public class CaptchaDao extends HibernateDao<Captcha, String> {

}
