package org.jfantasy.member.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.member.bean.enums.BillType;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 钱包账单
 */
@Entity
@Table(name = "WALLET_BILL")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class WalletBill extends BaseBusEntity {
    /**
     * 流水号
     */
    @Id
    @Column(name = "ID", nullable = false, updatable = false, precision = 22)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    /**
     * 金额
     */
    @Column(name = "AMOUNT", nullable = false, updatable = false)
    private BigDecimal amount;
    /**
     * 账单类型(收入/支出)
     */
    @Column(name = "TYPE", nullable = false, updatable = false)
    private BillType type;
    /**
     * 交易项目
     */
    @Column(name = "PROJECT", length = 500, nullable = false, updatable = false)
    private String project;
    /**
     * 交易流水
     */
    @Column(name = "TRADE_NO", nullable = false, updatable = false)
    private String tradeNo;
    /**
     * 交易时间
     */
    @Column(name = "TRADE_TIME", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date tradeTime;
    /**
     * 备注
     */
    @Column(name = "NOTES", length = 500, nullable = false, updatable = false)
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
