package org.jfantasy.member.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.hibernate.criterion.Restrictions;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.util.common.DateUtil;
import org.jfantasy.member.bean.Member;
import org.jfantasy.member.bean.Wallet;
import org.jfantasy.member.bean.WalletBill;
import org.jfantasy.member.bean.enums.BillStatus;
import org.jfantasy.member.bean.enums.BillType;
import org.jfantasy.member.dao.MemberDao;
import org.jfantasy.member.dao.WalletBillDao;
import org.jfantasy.member.dao.WalletDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class WalletService {

    @Autowired
    private WalletDao walletDao;
    @Autowired
    private WalletBillDao walletBillDao;
    @Autowired
    private MemberDao memberDao;

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
        return this.walletDao.findUnique(Restrictions.eq("id", memberId));
    }

    private Wallet getWallet(String account) {
        Wallet wallet = this.walletDao.findUnique(Restrictions.eq("account", account));
        if (wallet == null) {
            wallet = new Wallet();
            wallet.setAmount(BigDecimal.ZERO);
            wallet.setAccount(account);
            wallet.setIncome(BigDecimal.ZERO);
            wallet = this.walletDao.save(wallet);
        }
        return wallet;
    }

    public Pager<Wallet> findPager(Pager<Wallet> pager, List<PropertyFilter> filters) {
        return this.walletDao.findPager(pager, filters);
    }

    public void saveOrUpdateBill(JsonNode transaction) {
        //获取并初始化账户
        Wallet from = this.getWallet(transaction.get("from").asText());
        Wallet to = this.getWallet(transaction.get("to").asText());

        // 交易数据
        String tradeSn = transaction.get("sn").asText();
        BigDecimal tradeAmount = transaction.get("amount").decimalValue();
        String tradeNotes = transaction.get("notes").asText();
        String tradeStatus = transaction.get("status").asText();
        Date tradeTime = DateUtil.parse(transaction.get("createTime").asText());
        String project = transaction.get("project").get("key").asText();

        WalletBill bill = this.walletBillDao.findUnique(Restrictions.eq("wallet.id", from.getId()), Restrictions.eq("tradeNo", tradeSn));
        if (bill == null) {//添加转出交易
            bill = new WalletBill();
            bill.setTradeNo(tradeSn);
            bill.setType(BillType.out);
            bill.setAmount(tradeAmount);
            bill.setSummary(tradeNotes);
            bill.setStatus(BillStatus.getStatusByTradeStatus(tradeStatus));
            bill.setTradeTime(tradeTime);
            bill.setProject(project);
            this.walletBillDao.save(bill);
        }

        bill = this.walletBillDao.findUnique(Restrictions.eq("wallet.id", to.getId()), Restrictions.eq("tradeNo", tradeSn));
        if (bill == null) {//添加转入交易
            bill = new WalletBill();
            bill.setTradeNo(tradeSn);
            bill.setType(BillType.in);
            bill.setAmount(tradeAmount);
            bill.setSummary(tradeNotes);
            bill.setStatus(BillStatus.getStatusByTradeStatus(tradeStatus));
            bill.setTradeTime(tradeTime);
            bill.setProject(project);
            this.walletBillDao.save(bill);
        }

    }

    public WalletBill getBill(Long id) {
        return this.walletBillDao.get(id);
    }

    public Pager<WalletBill> findBillPager(Pager<WalletBill> pager, List<PropertyFilter> filters) {
        return this.walletBillDao.findPager(pager, filters);
    }
}
