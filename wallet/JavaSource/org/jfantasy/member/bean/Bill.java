package org.jfantasy.member.bean;

import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.member.bean.enums.BillType;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 钱包账单
 */
public class Bill extends BaseBusEntity {
    /**
     *
     */
    private Long id;
    /**
     * 金额
     */
    private BigDecimal amount;
    /**
     * 账单类型(收入/支出)
     */
    private BillType type;
    /**
     * 交易项目
     */
    private String project;
    /**
     * 交易流水
     */
    private String tradeNo;
    /**
     * 交易时间
     */
    private Date tradeTime;
    /**
     * 备注
     */
    private String notes;

    public BillType getType() {
        return type;
    }

    public void setType(BillType type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public Date getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(Date tradeTime) {
        this.tradeTime = tradeTime;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
