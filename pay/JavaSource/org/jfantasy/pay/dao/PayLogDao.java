package org.jfantasy.pay.dao;

import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.pay.bean.PayLog;
import org.springframework.stereotype.Repository;

@Repository
public class PayLogDao extends HibernateDao<PayLog, Long> {
}
