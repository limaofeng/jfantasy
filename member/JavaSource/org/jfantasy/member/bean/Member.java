package org.jfantasy.member.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.spring.validation.RESTful.POST;
import org.jfantasy.framework.spring.validation.RESTful.PUT;
import org.jfantasy.security.bean.Role;
import org.jfantasy.security.bean.UserGroup;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.*;

/**
 * 会员
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-9-22 上午11:25:14
 */
@ApiModel("会员信息")
@Entity
@Table(name = "MEM_MEMBER", uniqueConstraints = {
        @UniqueConstraint(name = "UK_MEMBER_TARGET", columnNames = {"TARGET_TYPE", "TARGET_ID"})
})
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler", "user_groups", "roles", "authorities", "details"})
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Member extends BaseBusEntity {

    public static final String MEMBER_TYPE_PERSONAL = "personal";
    public static final String MEMBER_TYPE_TEAM = "team";

    @Null(groups = {POST.class})
    @Id
    @Column(name = "ID", nullable = false, updatable = false, precision = 22, scale = 0)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    /**
     * 用户类型
     */
    @NotNull(groups = {POST.class})
    @Column(name = "MEMBER_TYPE", length = 20, nullable = false, updatable = false)
    private String type;
    /**
     * 用户登录名称
     */
    @NotNull(groups = {POST.class, PUT.class})
    @Length(min = 8, max = 20, groups = {POST.class, PUT.class})
    @ApiModelProperty("登录名称")
    @Column(name = "USERNAME", length = 20, nullable = false, unique = true)
    private String username;
    /**
     * 登录密码
     */
    @NotNull(groups = {POST.class})
    @ApiModelProperty("登录密码")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
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
    @JsonProperty("non_expired")
    @Column(name = "NON_EXPIRED")
    private boolean accountNonExpired;
    /**
     * 未锁定
     */
    @ApiModelProperty("未锁定")
    @JsonProperty("non_locked")
    @Column(name = "NON_LOCKED")
    private boolean accountNonLocked;
    /**
     * 未失效
     */
    @ApiModelProperty("未失效")
    @JsonProperty("credentials_non_expired")
    @Column(name = "CREDENTIALS_NON_EXPIRED")
    private boolean credentialsNonExpired;
    /**
     * 锁定时间
     */
    @ApiModelProperty("锁定时间")
    @JsonProperty("lock_time")
    @Column(name = "LOCK_TIME")
    private Date lockTime;
    /**
     * 最后登录时间
     */
    @ApiModelProperty("最后登录时间")
    @JsonProperty("last_login_time")
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
    /**
     * 目标Id
     */
    @Column(name = "TARGET_ID", length = 20)
    @JsonProperty("target_id")
    private String targetId;
    /**
     * 目标类型
     */
    @Column(name = "TARGET_TYPE", length = 10)
    @JsonProperty("target_type")
    private String targetType;
    @Transient
    @ApiModelProperty(value = "授权码", notes = "用于 oauth 授权时使用,使用时限 10 分钟")
    private String code;

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
    public String[] getAuthorities() {
        Set<String> authorities = new LinkedHashSet<>();
        for (UserGroup userGroup : this.getUserGroups()) {
            if (!userGroup.isEnabled()) {
                continue;
            }
            authorities.add(userGroup.getAuthority());
            authorities.addAll(Arrays.asList(userGroup.getRoleAuthorities()));
        }
        // 添加角色权限
        for (Role role : this.getRoles()) {
            if (!role.isEnabled()) {
                continue;
            }
            authorities.add(role.getAuthority());
        }
        return authorities.toArray(new String[authorities.size()]);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
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
