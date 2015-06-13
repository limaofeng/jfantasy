package com.fantasy.security.bean;

import com.fantasy.framework.dao.BaseBusEntity;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.security.SpringSecurityUtils;
import com.fantasy.security.userdetails.FantasyUserDetails;
import com.fantasy.system.bean.Website;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.hibernate.annotations.Cache;
import javax.persistence.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "AUTH_USER")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler","userGroups", "menus", "authorities" ,"logoImageStore"})
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class User extends BaseBusEntity implements FantasyUserDetails {

	private static final long serialVersionUID = 5507435998232223911L;

	@Id
	@Column(name = "ID", nullable = false, insertable = true, updatable = false, precision = 22, scale = 0)
	@GeneratedValue(generator = "fantasy-sequence")
	@GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
	private Long id;

	/**
	 * 用户登录名称
	 */
	@Column(name = "USERNAME", length = 20, nullable = false, unique = true)
	private String username;
	/**
	 * 登录密码
	 */
	@Column(name = "PASSWORD", length = 20, nullable = false)
	private String password;
	/**
	 * 用户显示昵称
	 */
	@Column(name = "NICK_NAME", length = 50)
	private String nickName;
	/**
	 * 是否启用
	 */
	@Column(name = "ENABLED")
	private boolean enabled;
	/**
	 * 未过期
	 */
	@Column(name = "NON_EXPIRED")
	private boolean accountNonExpired;
	/**
	 * 未锁定
	 */
	@Column(name = "NON_LOCKED")
	private boolean accountNonLocked;
	/**
	 * 未失效
	 */
	@Column(name = "CREDENTIALS_NON_EXPIRED")
	private boolean credentialsNonExpired;
	/**
	 * 锁定时间
	 */
	@Column(name = "LOCK_TIME")
	private Date lockTime;
	/**
	 * 最后登录时间
	 */
	@Column(name = "LAST_LOGIN_TIME")
	private Date lastLoginTime;
	/**
	 * 用户对应的用户组
	 */
	@ManyToMany(targetEntity = UserGroup.class, fetch = FetchType.LAZY)
	@JoinTable(name = "AUTH_USERGROUP_USER", joinColumns = @JoinColumn(name = "USER_ID"), inverseJoinColumns = @JoinColumn(name = "USERGROUP_ID"),foreignKey = @ForeignKey(name="FK_USERGROUP_USER_US"))
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	private List<UserGroup> userGroups;
	/**
	 * 用户对应的角色
	 */
	@ManyToMany(targetEntity = Role.class, fetch = FetchType.LAZY)
	@JoinTable(name = "AUTH_ROLE_USER", joinColumns = @JoinColumn(name = "USER_ID"), inverseJoinColumns = @JoinColumn(name = "ROLE_CODE"),foreignKey = @ForeignKey(name="FK_ROLE_USER_UID"))
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	private List<Role> roles;
	/**
	 * 用户详细信息
	 */
	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@PrimaryKeyJoinColumn
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	private UserDetails details;
	/**
	 * 用户允许访问的权限
	 */
	@Transient
	private List<Menu> menus;
    /**
     * 对应的网站信息
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="WEBSITE_ID",foreignKey = @ForeignKey(name="FK_WEBSITE_USER"))
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Website website;

    /**
     * 对应的组织机构
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="ORGANIZATION_ID",foreignKey = @ForeignKey(name="FK_ORGANIZATION_USER"))
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Organization organization;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void setUserGroups(List<UserGroup> userGroups) {
		this.userGroups = userGroups;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	public List<UserGroup> getUserGroups() {
		return this.userGroups;
	}

	public Date getLockTime() {
		return this.lockTime;
	}

	public void setLockTime(Date lockTime) {
		this.lockTime = lockTime;
	}

	public Date getLastLoginTime() {
		return this.lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public void setDetails(UserDetails details) {
		this.details = details;
		this.details.setUser(this);
	}

	public UserDetails getDetails() {
		return details;
	}

	@JsonIgnore
	public String[] getRoleCodes() {
		return ObjectUtil.toFieldArray(this.getRoles(), "code", String.class);
	}

	public List<Menu> getMenus() {
		if (this.menus == null) {
			this.menus = new ArrayList<Menu>();
			if(this.getRoles() != null) {
				for (Role role : this.getRoles()) {
					ObjectUtil.join(this.menus, role.getMenus(), "id");
				}
			}
			if(this.getUserGroups() != null) {
				for (UserGroup userGroup : this.getUserGroups()) {
					ObjectUtil.join(this.menus, userGroup.getMenus(), "id");
				}
			}
		}
		return this.menus;
	}

	public static class UserRolesSerialize extends JsonSerializer<List<Role>> {

		@Override
		public void serialize(List<Role> roles, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
			jgen.writeString(ObjectUtil.toString(roles, "name", ";"));
		}

	}

    public Website getWebsite() {
        return website;
    }

    public void setWebsite(Website website) {
        this.website = website;
    }

    @Transient
	public Collection<GrantedAuthority> getAuthorities() {
		return SpringSecurityUtils.getAuthorities(this);
	}

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", nickName='" + nickName + '\'' +
                ", enabled=" + enabled +
                ", accountNonExpired=" + accountNonExpired +
                ", accountNonLocked=" + accountNonLocked +
                ", credentialsNonExpired=" + credentialsNonExpired +
                ", lockTime=" + lockTime +
                ", lastLoginTime=" + lastLoginTime +
                ", details=" + details +
                '}';
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }
}