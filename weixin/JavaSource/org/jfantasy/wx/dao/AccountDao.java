package org.jfantasy.wx.dao;

import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.wx.bean.Account;
import org.springframework.stereotype.Repository;

@Repository("wx.AccountDao")
public class AccountDao extends HibernateDao<Account, String> {
}
