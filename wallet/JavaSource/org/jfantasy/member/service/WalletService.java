package org.jfantasy.member.service;

import org.hibernate.criterion.Restrictions;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.member.bean.Member;
import org.jfantasy.member.bean.Wallet;
import org.jfantasy.member.bean.WalletBill;
import org.jfantasy.member.dao.WalletBillDao;
import org.jfantasy.member.dao.WalletDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class WalletService {

    @Autowired
    private WalletDao walletDao;
    @Autowired
    private WalletBillDao walletBillDao;

    /**
     * 创建 用户钱包
     *
     * @param member  用户
     * @param account 资金账号
     * @param amount  资金余额(应该与账号一致)
     * @return Wallet
     */
    public Wallet newWallet(Member member, String account, BigDecimal amount) {
        Wallet wallet = new Wallet();
        wallet.setMember(member);
        wallet.setAccount(account);
        wallet.setAmount(amount);
        wallet.setIncome(BigDecimal.ZERO);
        //初始化账单 并 计算收益
        return walletDao.insert(wallet);
    }

    public Wallet getWallet(Long memberId) {
        return this.walletDao.findUnique(Restrictions.eq("member.id", memberId));
    }

    public Pager<WalletBill> findPager(Pager<WalletBill> pager, List<PropertyFilter> filters) {
        return this.walletBillDao.findPager(pager, filters);
    }

}
