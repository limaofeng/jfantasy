package org.jfantasy.wx.service;

import org.jfantasy.wx.bean.Account;
import org.jfantasy.wx.bean.AccountMapping;
import org.jfantasy.wx.dao.AccountMappingDao;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class AccountMappingService {

    @Autowired
    private AccountMappingDao accountMappingDao;

    public Account getAccount(String usertype, String username) {
        AccountMapping mapping = this.accountMappingDao.findUnique(Restrictions.eq("userType", usertype), Restrictions.eq("username", username));
        return mapping == null ? null : mapping.getAccount();
    }

}
