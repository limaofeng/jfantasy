package org.jfantasy.member.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.hibernate.criterion.Restrictions;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.httpclient.HttpClientUtil;
import org.jfantasy.framework.httpclient.Request;
import org.jfantasy.framework.httpclient.Response;
import org.jfantasy.framework.jackson.JSON;
import org.jfantasy.framework.spring.mvc.error.RestException;
import org.jfantasy.framework.spring.mvc.error.ValidationException;
import org.jfantasy.framework.util.common.DateUtil;
import org.jfantasy.member.bean.Card;
import org.jfantasy.member.bean.Member;
import org.jfantasy.member.bean.Wallet;
import org.jfantasy.member.bean.WalletBill;
import org.jfantasy.member.bean.enums.BillStatus;
import org.jfantasy.member.bean.enums.BillType;
import org.jfantasy.member.dao.CardDao;
import org.jfantasy.member.dao.MemberDao;
import org.jfantasy.member.dao.WalletBillDao;
import org.jfantasy.member.dao.WalletDao;
import org.jfantasy.oauth.userdetails.enums.Scope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@Service
public class WalletService {

    private final static Log LOG = LogFactory.getLog(WalletService.class);

    @Autowired
    private WalletDao walletDao;
    @Autowired
    private WalletBillDao walletBillDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private CardDao cardDao;

    private Wallet newWallet(String account, BigDecimal amount) {
        return newWallet(null, account, amount);
    }

    /**
     * 创建 用户钱包
     *
     * @param member  用户
     * @param account 资金账号
     * @param amount  资金余额(应该与账号一致)
     * @return Wallet
     */
    private Wallet newWallet(Member member, String account, BigDecimal amount) {
        Wallet wallet = new Wallet();
        wallet.setMember(member);
        wallet.setAccount(account);
        wallet.setAmount(amount);
        wallet.setIncome(BigDecimal.ZERO);
        //初始化账单 并 计算收益
        return walletDao.insert(wallet);
    }

    @Transactional
    public Wallet save(Member member) {
        Wallet wallet = this.walletDao.findUnique(Restrictions.eq("member", member));
        if (wallet == null) {
            String owner = Scope.member + ":" + member.getUsername();
            //1、检查账号信息
            try {
                Response response = HttpClientUtil.doGet("http://localhost:8080/accounts?EQS_owner=" + owner);
                if (response.getStatusCode() != 200) {
                    throw new ValidationException(203.1f, "检查账号出错");
                }
                JsonNode node = response.json().get("items");
                JsonNode account = node.get(0);
                if (account == null) {
                    Map<String, Object> data = new HashMap<>();
                    data.put("type", "personal");
                    data.put("owner", owner);
                    Request request = new Request(new StringEntity(JSON.serialize(data), ContentType.APPLICATION_JSON));
                    response = HttpClientUtil.doPost("http://localhost:8080/accounts", request);
                    if (response.getStatusCode() != 201) {
                        throw new ValidationException(203.2f, "创建账号出错");
                    }
                    account = response.json();
                }
                wallet = new Wallet();
                wallet.setMember(member);
                wallet.setAccount(account.get("sn").asText());
                wallet.setAmount(BigDecimal.valueOf(account.get("amount").asDouble()));
                wallet.setIncome(BigDecimal.ZERO);
                wallet.setGrowth(0L);
                wallet.setPoints(0L);
                wallet.setBills(Collections.<WalletBill>emptyList());
                this.walletDao.save(wallet);
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
                throw new ValidationException(203.3f, "网络问题!");
            }

        }
        return wallet;
    }

    @Transactional
    public Wallet getWalletByOwner(String owner) {
        String[] asr = owner.split(":");
        String username = asr[1];
        switch (asr[0]) {
            case "member":
                return this.save(memberDao.findUnique(Restrictions.eq("username", username)));
        }
        return null;
    }

    @Transactional
    public Wallet getWalletByMember(Long memberId) {
        return this.save(memberDao.get(memberId));
    }

    public Wallet getWallet(Long id) {
        return this.walletDao.findUnique(Restrictions.eq("id", id));
    }

    private Wallet getWallet(String account) {
        Wallet wallet = this.walletDao.findUnique(Restrictions.eq("account", account));
        if (wallet == null) {
            return newWallet(account, BigDecimal.ZERO);
        }
        return wallet;
    }

    public Pager<Wallet> findPager(Pager<Wallet> pager, List<PropertyFilter> filters) {
        return this.walletDao.findPager(pager, filters);
    }

    @Transactional
    public Wallet saveOrUpdateWallet(JsonNode account) {
        String account_sn = account.get("sn").asText();
        BigDecimal account_amount = account.get("amount").decimalValue();
        String account_type = account.get("type").asText();
        String account_owner = account.get("owner").asText();

        Wallet wallet = this.walletDao.findUnique(Restrictions.eq("account", account_sn));
        if (wallet == null) {
            switch (account_type) {
                case "personal":
                    String username = account_owner.replaceAll("^[^:]+:", "");
                    Member member = memberDao.findUnique(Restrictions.eq("username", username));
                    if (member == null) {
                        throw new RestException("用户不存在!");
                    }
                    return newWallet(member, account_sn, account_amount);
                case "platform":
                case "enterprise":
                    return newWallet(account_sn, account_amount);
            }
        } else {
            return updateWallet(wallet.getId(), account_amount);
        }
        throw new RestException("创建账号失败!");
    }

    private Wallet updateWallet(Long id, BigDecimal amount) {
        Wallet wallet = this.walletDao.get(id);
        wallet.setAmount(amount);
        this.walletDao.update(wallet);
        return wallet;
    }

    @Transactional
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

    @Transactional
    public void addCard(String owner, Card card) {
        Wallet wallet = this.getWalletByOwner(owner);
        card.setWallet(wallet);
        this.cardDao.save(card);
    }

}
