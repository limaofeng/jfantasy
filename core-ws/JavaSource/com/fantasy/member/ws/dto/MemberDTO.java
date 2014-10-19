package com.fantasy.member.ws.dto;

import java.io.Serializable;
import java.util.Date;

public class MemberDTO implements Serializable {

	private static final long serialVersionUID = 7115437249356814947L;
	
	
	private Long id;
	/**
	 * 用户登录名称
	 */
	private String username;
	/**
	 * 登录密码
	 */
	private String password;
	/**
	 * 用户显示昵称
	 */
	private String nickName;

	/**
	 * 是否启用
	 */
	private boolean enabled;

	/**
	 * 未过期
	 */
	private boolean accountNonExpired;
	/**
	 * 未锁定
	 */
	private boolean accountNonLocked;
	/**
	 * 未失效
	 */
	private boolean credentialsNonExpired;
	/**
	 * 锁定时间
	 */
	private Date lockTime;
	/**
	 * 最后登录时间
	 */
	private Date lastLoginTime;
	/**
	 * 会员详细信息
	 */
	private MemberDetailsDTO details;

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

	public MemberDetailsDTO getDetails() {
		return details;
	}

	public void setDetails(MemberDetailsDTO details) {
		this.details = details;
	}

}
