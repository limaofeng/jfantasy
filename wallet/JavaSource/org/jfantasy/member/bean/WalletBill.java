package org.jfantasy.member.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.member.bean.enums.BillStatus;
import org.jfantasy.member.bean.enums.BillType;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 钱包账单
 */
@Entity
@Table(name = "MEM_WALLET_BILL")
@TableGenerator(name = "walletbill_gen", table = "sys_sequence", pkColumnName = "gen_name", pkColumnValue = "mem_wallet_bill:id", valueColumnName = "gen_value", allocationSize = 10)
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler", "wallet"})
public class WalletBill extends BaseBusEntity {

    private static final long serialVersionUID = -8229079024927337837L;

    /**
     * 流水号
     */
    @Id
    @Column(name = "ID", nullable = false, updatable = false, precision = 22)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "walletbill_gen")
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
    @Enumerated(EnumType.STRING)
    private BillType type;
    /**
     * 账单状态
     */
    @Column(name = "STATUS", nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private BillStatus status;
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
     * 摘要
     */
    @Column(name = "SUMMARY", nullable = false, updatable = false)
    private String summary;
    /**
     * 钱包
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WALLET_ID", updatable = false, foreignKey = @ForeignKey(name = "FK_BILL_WALLET"))
    private Wallet wallet;

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

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public BillStatus getStatus() {
        return status;
    }

    public void setStatus(BillStatus status) {
        this.status = status;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }
}
