package org.jfantasy.oauth.userdetails;

import org.jfantasy.oauth.userdetails.enums.Scope;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class OAuthUserDetails implements UserDetails {

    private final static long serialVersionUID = 8427444478879938831L;

    public enum Type {
        /**
         * APP
         */
        app,
        /**
         * 用户
         */
        user,
        /**
         * 会员
         */
        member
    }

    /**
     * 主键
     */
    private Long id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 类型
     */
    private Type type;
    /**
     * 用户类型
     */
    private String userType;
    /**
     * 用于标记权限范围
     */
    private Scope scope;
    /**
     * 应用ID
     */
    private Long appId;
    /**
     * 应用名称
     */
    private String appName;
    /**
     * 应用授权Key
     */
    private String appKey;
    /**
     * 应用授权名称
     */
    private String appKeyName;
    /**
     * 用户显示名
     */
    private String nickName;
    /**
     * 权限
     */
    private List<GrantedAuthority> authorities;
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
     * 用于标示唯一<br/>
     * 结构如下: type:username
     * member:15921884771
     */
    private String key;
    /**
     * 平台简码
     */
    private String platform;
    /**
     * 目标类型
     */
    private String targetType;
    /**
     * 目标ID
     */
    private String targetId;

    public void setType(Type type) {
        this.type = type;
    }

    public OAuthUserDetails(Long appId, String appName, String appKey, String appKeyName, String platform) {
        this.appId = appId;
        this.appName = appName;
        this.appKey = appKey;
        this.appKeyName = appKeyName;
        this.platform = platform;
    }

    public Type getType() {
        return type;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserType() {
        return userType;
    }

    public Scope getScope() {
        return scope;
    }

    public Long getAppId() {
        return appId;
    }

    public String getAppName() {
        return appName;
    }

    public String getAppKey() {
        return appKey;
    }

    public String getAppKeyName() {
        return appKeyName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setAuthorities(List<GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return "******";
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPlatform() {
        return platform;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }
}
