package com.fantasy.wx.framework.account;

import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.wx.framework.exception.AppidNotFoundException;
import com.fantasy.wx.framework.session.AccountDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * 简单的微信公众号详情服务类
 */
public class SimpleAccountDetailsService implements AccountDetailsService {

    private List<AccountDetails> accounts = new ArrayList<AccountDetails>();

    @Override
    public AccountDetails loadAccountByAppid(String appid) throws AppidNotFoundException {
        AccountDetails accountDetails = ObjectUtil.find(accounts,"getAppId()",appid);
        if(accountDetails == null){
            throw new AppidNotFoundException(" appid 不存在 ");
        }
        return accountDetails;
    }

    @Override
    public List<AccountDetails> getAll() {
        return accounts;
    }

    public void setAccounts(List<AccountDetails> accounts) {
        this.accounts = accounts;
    }

}
