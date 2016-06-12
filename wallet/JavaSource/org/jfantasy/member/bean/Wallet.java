package org.jfantasy.member.bean;

import org.jfantasy.framework.dao.BaseBusEntity;

import java.math.BigDecimal;

/**
 * 钱包
 */
public class Wallet extends BaseBusEntity {
    /**
     * 钱包ID
     */
    private Long id;
    /**
     * 会员
     */
    private Member member;
    /**
     * 账户余额
     */
    private BigDecimal amount;
    /**
     * 累积收入
     */
    private BigDecimal income;
    /**
     * 资金账户
     */
    private String account;
    /**
     * 积分
     */
    private Long points;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Long getPoints() {
        return points;
    }

    public void setPoints(Long points) {
        this.points = points;
    }

}
