package com.fantasy.payment.dao;

import com.fantasy.framework.dao.hibernate.HibernateDao;
import com.fantasy.payment.bean.Payment;
import org.springframework.stereotype.Repository;

@Repository
public class PaymentDao extends HibernateDao<Payment, Long> {
}
