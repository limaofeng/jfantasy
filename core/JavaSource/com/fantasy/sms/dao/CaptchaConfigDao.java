package com.fantasy.sms.dao;

import org.springframework.stereotype.Repository;

import com.fantasy.framework.dao.hibernate.HibernateDao;
import com.fantasy.sms.bean.CaptchaConfig;

@Repository
public class CaptchaConfigDao extends HibernateDao<CaptchaConfig, String>{

}
