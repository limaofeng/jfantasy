package org.jfantasy.pay.dao;

import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.pay.bean.Transaction;
import org.springframework.stereotype.Repository;

@Repository
public class TransactionDao extends HibernateDao<Transaction, String> {
}
