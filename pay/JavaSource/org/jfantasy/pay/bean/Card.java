package org.jfantasy.pay.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.pay.bean.enums.CardStatus;
import org.jfantasy.pay.bean.enums.Usage;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 消费卡
 */
@Entity
@Table(name = "PAY_CARD")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Card extends BaseBusEntity {
    /**
     * 卡号
     */
    @ApiModelProperty("编号")
    @Id
    @Column(name = "NUMBER", length = 20)
    private String no;
    /**
     * 会员卡使用方式
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "_USAGE", length = 20, updatable = false)
    private Usage usage;
    /**
     * 会员卡类型
     */
    @ManyToOne
    @JoinColumn(name = "TYPE", updatable = false)
    private CardType type;
    /**
     * 卡设计
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DESIGN")
    private CardDesign design;
    /**
     * 批次
     */
    @ManyToOne
    @JoinColumn(name = "BATCH_NO")
    private CardBatch batch;
    /**
     * 卡状态
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", length = 20)
    private CardStatus status;
    /**
     * 密钥
     */
    @Column(name = "SECRET", length = 10)
    private String secret;
    /**
     * 金额
     */
    @Column(name = "AMOUNT", nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;
    /**
     * 销卡时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DESTROY_TIME")
    private Date destroyTime;

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public Usage getUsage() {
        return usage;
    }

    public void setUsage(Usage usage) {
        this.usage = usage;
    }

    public CardType getType() {
        return type;
    }

    public void setType(CardType type) {
        this.type = type;
    }

    public CardBatch getBatch() {
        return batch;
    }

    public void setBatch(CardBatch batch) {
        this.batch = batch;
    }

    public CardStatus getStatus() {
        return status;
    }

    public void setStatus(CardStatus status) {
        this.status = status;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getDestroyTime() {
        return destroyTime;
    }

    public void setDestroyTime(Date destroyTime) {
        this.destroyTime = destroyTime;
    }

    public CardDesign getDesign() {
        return design;
    }

    public void setDesign(CardDesign design) {
        this.design = design;
    }
}
