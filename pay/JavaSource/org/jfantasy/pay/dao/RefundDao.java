package org.jfantasy.pay.dao;

import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.pay.bean.Refund;
import org.springframework.stereotype.Repository;

@Repository
public class RefundDao  extends HibernateDao<Refund, String> {
}