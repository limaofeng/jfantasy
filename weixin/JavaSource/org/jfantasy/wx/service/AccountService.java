package org.jfantasy.wx.service;

import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.wx.framework.account.AccountDetailsService;
import org.jfantasy.wx.bean.Account;
import org.jfantasy.wx.dao.AccountDao;
import org.jfantasy.wx.framework.exception.AppidNotFoundException;
import org.jfantasy.wx.framework.session.AccountDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 数据库存储微信信息
 */
@Service
@Transactional
public class AccountService implements AccountDetailsService {

    @Autowired
    private AccountDao accountDao;

    @Override
    public AccountDetails loadAccountByAppid(String appid) throws AppidNotFoundException {
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
        return (List) accountDao.getAll();
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
        return accountDao.save(wc);
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
