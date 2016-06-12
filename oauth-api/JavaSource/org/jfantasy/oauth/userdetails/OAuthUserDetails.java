package org.jfantasy.oauth.userdetails;


import org.jfantasy.oauth.userdetails.enums.Scope;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class OAuthUserDetails implements UserDetails {

    /**
     * 主键
     */
    private Long id;
    /**
     * 用户名
     */
    private String username;
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

    public OAuthUserDetails(Long appId, String appName, String appKey, String appKeyName) {
        this.appId = appId;
        this.appName = appName;
        this.appKey = appKey;
        this.appKeyName = appKeyName;
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return "limaofeng";
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}