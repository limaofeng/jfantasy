package org.jfantasy.wx.bean;

import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.util.jackson.JSON;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 授权用户与公众号的对应关系
 */
@Entity
@Table(name = "WX_ACCOUNT_MAPPING", uniqueConstraints = {@UniqueConstraint(columnNames = {"USERNAME", "USER_TYPE"})})
@JsonFilter(JSON.CUSTOM_FILTER)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AccountMapping extends BaseBusEntity {

    @Id
    @Column(name = "ID", nullable = false, insertable = true, updatable = false, precision = 22, scale = 0)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    /**
     * 对应的用户名
     */
    @Column(name = "USERNAME", length = 200)
    private String username;
    /**
     * 对应的用户类型
     */
    @Column(name = "USER_TYPE", length = 200)
    private String userType;
    /**
     * 对应的公众号
     */
    @ManyToOne
    @JoinColumn(name = "ACCOUNT_ID")
    private Account account;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
