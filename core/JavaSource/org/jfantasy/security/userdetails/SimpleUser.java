package org.jfantasy.security.userdetails;

import org.jfantasy.framework.util.common.PropertiesHelper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class SimpleUser<T extends FantasyUserDetails> implements UserDetails {
    protected static final long serialVersionUID = 1031301459059227881L;
    private final static PropertiesHelper propertiesHelper = PropertiesHelper.load("props/security.properties");
    private T user;
    private Map<String, Object> data;

    public SimpleUser(T user) {
        this.user = user;
        this.data = new HashMap<String, Object>();
    }

    @SuppressWarnings("unchecked")
    public Collection<GrantedAuthority> getAuthorities() {
        return (Collection<GrantedAuthority>) this.user.getAuthorities();
    }

    public String getPassword() {
        return this.user.getPassword();
    }

    public String getUsername() {
        return this.user.getUsername();
    }

    public boolean isAccountNonExpired() {
        return this.user.isAccountNonExpired() || isDefault("accountNonExpired");
    }

    public boolean isAccountNonLocked() {
        return this.user.isAccountNonLocked() || isDefault("accountNonLocked");
    }

    public boolean isCredentialsNonExpired() {
        return this.user.isCredentialsNonExpired() || isDefault("credentialsNonExpired");
    }

    public boolean isEnabled() {
        return this.user.isEnabled() || isDefault("enabled");
    }

    private boolean isDefault(String key) {
        return propertiesHelper.getBoolean("security.user.default." + key, false);
    }

    /**
     * 获取用户绑定的具体对象
     *
     * @return
     * @功能描述
     */
    public T getUser() {
        return this.user;
    }

    /**
     * 更新绑定的对象,一般在当前用户更新自己的信息之后.
     *
     * @param user
     * @功能描述
     */
    public void setUser(T user) {
        this.user = user;
    }

    public void data(String key, Object value) {
        this.data.put(key, value);
    }

    public Object data(String key) {
        return this.data.get(key);
    }

    public Map<String, Object> getData() {
        return data;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public boolean equals(Object obj) {
        if (obj instanceof SimpleUser) {
            SimpleUser simpleUser = (SimpleUser) obj;
            return this.getUsername().equals(simpleUser.getUsername());
        } else {
            return super.equals(obj);
        }
    }

    @Override
    public int hashCode() {
        return this.getUsername().hashCode();
    }

}