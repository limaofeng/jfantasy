package org.jfantasy.pay.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.pay.bean.enums.AccountType;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "PAY_ACCOUNT")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Account extends BaseBusEntity {
    /**
     * 编号
     */
    @Id
    @Column(name = "ID", updatable = false)
    private String sn;
    /**
     * 账号类型
     */
    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    private AccountType type;
    /**
     * 账户余额
     */
    @Column(name = "AMOUNT", nullable = false,precision = 15, scale = 2)
    private BigDecimal amount;
    /**
     * 可用积分
     */
    @Column(name = "POINTS", nullable = false)
    private Long points;
    /**
     * 所有者
     */
    @Column(name = "owner")
    private String owner;

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public AccountType getType() {
        return type;
    }

    public void setType(AccountType type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Long getPoints() {
        return points;
    }

    public void setPoints(Long points) {
        this.points = points;
    }

}
