package org.jfantasy.pay.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.pay.bean.enums.TxChannel;
import org.jfantasy.pay.bean.enums.TxStatus;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 交易表
 */
@Entity
@Table(name = "PAY_TRANSACTION")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Transaction extends BaseBusEntity {
    /**
     * 交易流水号
     */
    @Id
    @Column(name = "SN", updatable = false)
    private String sn;
    /**
     * 转出账号<br/>
     * 线下渠道时 填写 线下转账账号<br/>
     * 第三方交易是 填写 PayConfigId
     */
    @Column(name = "FROM", nullable = false)
    private String from;
    /**
     * 转入账号<br/>
     * 线下渠道时 填写 线下转账账号<br/>
     * 第三方交易是 填写 PayConfigId
     */
    @Column(name = "TO", nullable = false)
    private String to;
    /**
     * 金额
     */
    @Column(name = "AMOUNT", nullable = false,updatable = false)
    private BigDecimal amount;
    /**
     * 交易渠道
     */
    @Column(name = "CHANNEL", nullable = false)
    private TxChannel channel;
    /**
     * 交易状态
     */
    @Column(name = "STATUS", nullable = false)
    private TxStatus status;
    /**
     * 备注
     */
    @Column(name = "NOTES")
    private String notes;

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public TxStatus getStatus() {
        return status;
    }

    public void setStatus(TxStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public TxChannel getChannel() {
        return channel;
    }

    public void setChannel(TxChannel channel) {
        this.channel = channel;
    }
}
