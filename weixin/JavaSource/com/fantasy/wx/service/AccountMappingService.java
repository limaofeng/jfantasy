package com.fantasy.wx.service;

import com.fantasy.wx.bean.Account;
import com.fantasy.wx.bean.AccountMapping;
import com.fantasy.wx.dao.AccountMappingDao;
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
