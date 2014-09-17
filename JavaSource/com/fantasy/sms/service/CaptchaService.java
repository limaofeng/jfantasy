package com.fantasy.sms.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.sms.bean.Captcha;
import com.fantasy.sms.dao.CaptchaDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
public class CaptchaService {

	@Resource
	private CaptchaDao captchaDao;


    public Pager<Captcha> findPager(Pager<Captcha> pager, List<PropertyFilter> filters) {
        return this.captchaDao.findPager(pager,filters);
    }

}
