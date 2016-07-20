package org.jfantasy.pay.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.pay.bean.enums.AccountStatus;
import org.jfantasy.pay.bean.enums.AccountType;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "PAY_ACCOUNT")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "password", "version"})
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
     * 账户状态
     */
    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private AccountStatus status;
    /**
     * 账户余额
     */
    @Column(name = "AMOUNT", nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;
    /**
     * 支付密码
     */
    @Column(name = "PASSWORD", length = 100)
    private String password;
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
    @Version
    @Column(name = "OPTLOCK")
    private Integer version;

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

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

    public Integer getVersion() {
        return version;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
