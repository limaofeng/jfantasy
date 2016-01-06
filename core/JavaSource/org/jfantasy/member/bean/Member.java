package org.jfantasy.member.bean;

import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.spring.validation.RESTful.*;
import org.jfantasy.framework.util.jackson.JSON;
import org.jfantasy.security.SpringSecurityUtils;
import org.jfantasy.security.bean.Role;
import org.jfantasy.security.bean.UserGroup;
import org.jfantasy.security.userdetails.FantasyUserDetails;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.NullSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 会员
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-9-22 上午11:25:14
 */
@ApiModel("会员信息")
@Entity
@Table(name = "MEM_MEMBER")
@JsonFilter(JSON.CUSTOM_FILTER)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "details", "userGroups", "roles", "authorities"})
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Member extends BaseBusEntity implements FantasyUserDetails {

    @Null(message = "创建用户时,请不要传入ID", groups = {POST.class})
    @Id
    @Column(name = "ID", nullable = false, insertable = true, updatable = false, precision = 22, scale = 0)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    /**
     * 用户登录名称
     */
    @NotNull(message = "登录名称不能为空", groups = {POST.class, PUT.class})
    @Length(min = 8, max = 20, message = "登录名称长度不合法,限制长度为 8-20 个字符", groups = {POST.class, PUT.class})
    @ApiModelProperty("登录名称")
    @Column(name = "USERNAME", length = 20, nullable = false, unique = true)
    private String username;
    /**
     * 登录密码
     */
    @ApiModelProperty("登录密码")
    @JsonSerialize(using = NullSerializer.class)
    @Column(name = "PASSWORD", length = 20, nullable = false)
    private String password;
    /**
     * 用户显示昵称
     */
    @ApiModelProperty("显示昵称")
    @Column(name = "NICK_NAME", length = 50)
    private String nickName;
    /**
     * 是否启用
     */
    @ApiModelProperty("是否启用")
    @Column(name = "ENABLED")
    private boolean enabled;
    /**
     * 未过期
     */
    @ApiModelProperty("未过期")
    @Column(name = "NON_EXPIRED")
    private boolean accountNonExpired;
    /**
     * 未锁定
     */
    @ApiModelProperty("未锁定")
    @Column(name = "NON_LOCKED")
    private boolean accountNonLocked;
    /**
     * 未失效
     */
    @ApiModelProperty("未失效")
    @Column(name = "CREDENTIALS_NON_EXPIRED")
    private boolean credentialsNonExpired;
    /**
     * 锁定时间
     */
    @ApiModelProperty("锁定时间")
    @Column(name = "LOCK_TIME")
    private Date lockTime;
    /**
     * 最后登录时间
     */
    @ApiModelProperty("最后登录时间")
    @Column(name = "LAST_LOGIN_TIME")
    private Date lastLoginTime;
    /**
     * 关联用户组
     */
    @ApiModelProperty(hidden = true)
    @ManyToMany(targetEntity = UserGroup.class, fetch = FetchType.LAZY)
    @JoinTable(name = "AUTH_USERGROUP_MEMBER", joinColumns = @JoinColumn(name = "MEMBER_ID"), inverseJoinColumns = @JoinColumn(name = "USERGROUP_ID"), foreignKey = @ForeignKey(name = "FK_USERGROUP_MEMBER_MEM"))
    private List<UserGroup> userGroups;
    /**
     * 关联角色
     */
    @ApiModelProperty(hidden = true)
    @ManyToMany(targetEntity = Role.class, fetch = FetchType.LAZY)
    @JoinTable(name = "AUTH_ROLE_MEMBER", joinColumns = @JoinColumn(name = "MEMBER_ID"), inverseJoinColumns = @JoinColumn(name = "ROLE_CODE"), foreignKey = @ForeignKey(name = "FK_ROLE_MEMBER_MID"))
    private List<Role> roles;
    /**
     * 会员其他信息
     */
    @ApiModelProperty("会员详细信息")
    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @PrimaryKeyJoinColumn
    private MemberDetails details;

    public Member() {
    }

    public Member(Long id) {
        this.id = id;
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public Date getLockTime() {
        return lockTime;
    }

    public void setLockTime(Date lockTime) {
        this.lockTime = lockTime;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public List<UserGroup> getUserGroups() {
        return userGroups;
    }

    public void setUserGroups(List<UserGroup> userGroups) {
        this.userGroups = userGroups;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public MemberDetails getDetails() {
        return details;
    }

    public void setDetails(MemberDetails details) {
        this.details = details;
        if (details != null) {
            this.details.setMember(this);
        }
    }

    @Transient
    @ApiModelProperty(hidden = true)
    public Collection<GrantedAuthority> getAuthorities() {
        return SpringSecurityUtils.getAuthorities(this);
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", nickName='" + nickName + '\'' +
                ", enabled=" + enabled +
                ", accountNonExpired=" + accountNonExpired +
                ", accountNonLocked=" + accountNonLocked +
                ", credentialsNonExpired=" + credentialsNonExpired +
                ", lockTime=" + lockTime +
                ", lastLoginTime=" + lastLoginTime +
                ", userGroups=" + userGroups +
                ", roles=" + roles +
                ", details=" + details +
                '}';
    }
}
