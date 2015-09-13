package com.fantasy.sms.dao;

import org.springframework.stereotype.Repository;

import com.fantasy.framework.dao.hibernate.HibernateDao;
import com.fantasy.sms.bean.Captcha;

@Repository
public class CaptchaDao extends HibernateDao<Captcha, String> {

}
