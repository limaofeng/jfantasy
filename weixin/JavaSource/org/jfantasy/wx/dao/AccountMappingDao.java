package org.jfantasy.wx.dao;

import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.wx.bean.AccountMapping;
import org.springframework.stereotype.Repository;

@Repository
public class AccountMappingDao extends HibernateDao<AccountMapping, Long> {
}
