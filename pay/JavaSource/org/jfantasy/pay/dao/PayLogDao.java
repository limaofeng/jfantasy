package org.jfantasy.pay.dao;

import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.pay.bean.PaymentLog;
import org.springframework.stereotype.Repository;

@Repository
public class PayLogDao extends HibernateDao<PaymentLog, Long> {
}
