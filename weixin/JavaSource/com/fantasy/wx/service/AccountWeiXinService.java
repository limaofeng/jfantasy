package com.fantasy.wx.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.wx.framework.account.AccountDetailsService;
import com.fantasy.wx.bean.Account;
import com.fantasy.wx.dao.AccountDao;
import com.fantasy.wx.framework.exception.AppidNotFoundException;
import com.fantasy.wx.framework.session.AccountDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 数据库存储微信信息
 */
@Service
@Transactional
public class AccountWeiXinService implements AccountDetailsService {

    @Autowired
    private AccountDao accountDao;

    @Override
    public AccountDetails loadAccountByAppid(String appid) throws AppidNotFoundException {
        /*return this.getAll().get(0);*/
        AccountDetails accountDetails = get(appid);
        if (accountDetails == null) {
            throw new AppidNotFoundException(" appid 不存在 ");
        }
        return accountDetails;
    }

    /**
     * 查找所有配置信息
     *
     * @return List<AccountDetails>
     */
    public List<AccountDetails> getAll() {

        /*
         List<AccountDetails> accountDetailses = new ArrayList<AccountDetails>();Account account = new Account();
        account.setAppId("wxcbc2c9fb9d585cd3");
        account.setSecret("4b224fb5b08f2380572e45baecda63ba");
        account.setType(AccountDetails.Type.service);
        account.setToken("haolue_token");
        account.setAesKey("tUQwZUkxaiRFF14lLqjjIV53JaVaPtyoe0NEn8otai6");
        account.setPrimitiveId("gh_3d6114f11c71");
        accountDetailses.add(account);*/
        //accountDetailses.addAll(accounts);
        List accounts = accountDao.getAll();
        return accounts;
    }

    /**
     * 列表查询
     *
     * @param pager   分页
     * @param filters 查询条件
     * @return 分页对象
     */
    public Pager<Account> findPager(Pager<Account> pager, List<PropertyFilter> filters) {
        return this.accountDao.findPager(pager, filters);
    }

    /**
     * 保存微信配置信息对象
     *
     * @param wc appid
     * @return 微信配置信息对象
     */
    public Account save(Account wc) {
        accountDao.save(wc);
        return wc;
    }

    /**
     * 根据id 批量删除
     *
     * @param ids appid
     */
    public void delete(String... ids) {
        for (String id : ids) {
            Account m = this.accountDao.get(id);
            this.accountDao.delete(m);
        }
    }

    /**
     * 通过id查找微信配置对象
     *
     * @param id appid
     * @return 微信配置对象
     */
    public Account get(String id) {
        return this.accountDao.get(id);
    }

}
