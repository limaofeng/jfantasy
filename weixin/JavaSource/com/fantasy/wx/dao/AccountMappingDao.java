package com.fantasy.wx.dao;

import com.fantasy.framework.dao.hibernate.HibernateDao;
import com.fantasy.wx.bean.AccountMapping;
import org.springframework.stereotype.Repository;

@Repository
public class AccountMappingDao extends HibernateDao<AccountMapping, Long> {
}
