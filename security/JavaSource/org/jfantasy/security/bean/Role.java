package org.jfantasy.security.bean;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.framework.util.jackson.JSON;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "AUTH_ROLE")
@JsonFilter(JSON.CUSTOM_FILTER)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "menus", "permissions", "users", "members","roleAuthorities"})
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Role extends BaseBusEntity {

    private static final long serialVersionUID = 4870450046611332600L;

    /**
     * 角色编码
     */
    @Id
    @Column(name = "CODE")
    private String code;
    /**
     * 角色名称
     */
    @Column(name = "NAME")
    private String name;
    /**
     * 角色类型，用于区分不同类型的角色。比如：后台管理与前台会员之间的角色
     */
    @Column(name = "TYPE", length = 20)
    private String type;
    /**
     * 是否启用 0禁用 1 启用
     */
    @Column(name = "ENABLED")
    private Boolean enabled;
    /**
     * 描述信息
     */
    @Column(name = "DESCRIPTION")
    private String description;

    /**
     * 角色对应的菜单
     */
    @ManyToMany(targetEntity = Menu.class, fetch = FetchType.LAZY)
    @JoinTable(name = "AUTH_ROLE_MENU", joinColumns = @JoinColumn(name = "ROLE_CODE"), inverseJoinColumns = @JoinColumn(name = "MENU_ID"),foreignKey = @ForeignKey(name = "FK_ROLE_MENU_RCODE") )
    private List<Menu> menus;

    /**
     * 角色对应的资源
     */
    @ManyToMany(targetEntity = Permission.class, fetch = FetchType.LAZY)
    @JoinTable(name = "AUTH_ROLE_PERMISSION", joinColumns = @JoinColumn(name = "ROLE_CODE"), inverseJoinColumns = @JoinColumn(name = "PERMISSION_ID"))
    private List<Permission> permissions;

    @ManyToMany(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinTable(name = "AUTH_ROLE_USER", joinColumns = @JoinColumn(name = "ROLE_CODE"), inverseJoinColumns = @JoinColumn(name = "USER_ID"),foreignKey = @ForeignKey(name = "FK_ROLE_USER_RCODE") )
    private List<User> users;

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getEnabled() {
        return this.enabled;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public Boolean isEnabled() {
        return this.enabled;
    }

    @JsonIgnore
    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    public List<Menu> getMenus() {
        return menus;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public void setMenus(List<Menu> menus) {
        this.menus = menus;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<GrantedAuthority> getRoleAuthorities() {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + getCode()));
        return grantedAuthorities;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Role) {
            return this.code.equals(((Role) obj).getCode());
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return StringUtil.isNotBlank(code) ? code.hashCode() : super.hashCode();
    }

    @JsonIgnore
    public List<GrantedAuthority> getMenuAuthorities() {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
        if (ObjectUtil.isNull(getMenus())) {
            return grantedAuthorities;
        }
        for (Menu menu : getMenus()) {
            grantedAuthorities.add(menu.getMenuAuthoritie());
        }
        return grantedAuthorities;
    }

}