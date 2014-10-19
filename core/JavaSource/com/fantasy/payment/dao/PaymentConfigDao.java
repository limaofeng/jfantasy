package com.fantasy.payment.dao;

import org.springframework.stereotype.Repository;
import com.fantasy.framework.dao.hibernate.HibernateDao;
import com.fantasy.payment.bean.PaymentConfig;

/**
 *@Author lsz
 *@Date 2013-12-18 下午5:18:39
 *
 */
@Repository
public class PaymentConfigDao extends HibernateDao<PaymentConfig, Long> {

}

