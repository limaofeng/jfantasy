package org.jfantasy.pay.dao;

import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.pay.bean.Account;
import org.springframework.stereotype.Repository;

@Repository
public class AccountDao extends HibernateDao<Account,String> {
}
