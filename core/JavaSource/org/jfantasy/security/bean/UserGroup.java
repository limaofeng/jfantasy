package org.jfantasy.security.bean;

import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.jackson.JSON;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "AUTH_USERGROUP")
@JsonFilter(JSON.CUSTOM_FILTER)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "menus", "permissions", "roles", "permissions"})
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UserGroup extends BaseBusEntity {

    private static final long serialVersionUID = 7898475330929818969L;

    @Id
    @Column(name = "ID", nullable = false, insertable = true, updatable = true, precision = 22, scale = 0)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;

    /**
     * 用户组编码
     */
    @Column(name = "CODE")
    private String code;
    /**
     * 用户组名称
     */
    @Column(name = "NAME")
    private String name;
    /**
     * 是否启用
     */
    @Column(name = "ENABLED")
    private Boolean enabled;
    /**
     * 描述
     */
    @Column(name = "DESCRIPTION")
    private String description;

    /**
     * 用户组对应的菜单
     */
    @ManyToMany(targetEntity = Menu.class, fetch = FetchType.LAZY)
    @JoinTable(name = "AUTH_USERGROUP_MENU", joinColumns = @JoinColumn(name = "USERGROUP_ID"), inverseJoinColumns = @JoinColumn(name = "MENU_ID"), foreignKey = @ForeignKey(name = "FK_USERGROUP_MENU_UG"))
    private List<Menu> menus;

    /**
     * 用户组对应的资源
     */
    @ManyToMany(targetEntity = Permission.class, fetch = FetchType.LAZY)
    @JoinTable(name = "AUTH_USERGROUP_PERMISSION", joinColumns = @JoinColumn(name = "USERGROUP_ID"), inverseJoinColumns = @JoinColumn(name = "PERMISSION_ID"), foreignKey = @ForeignKey(name = "FK_USERGROUP_PERMISSION_UG"))
    private List<Permission> permissions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
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

    public List<Role> getRoles() {
        return new ArrayList<Role>();
    }

    public Boolean isEnabled() {
        return this.enabled;
    }

    public List<Menu> getMenus() {
        return this.menus;
    }

    public void setMenus(List<Menu> menus) {
        this.menus = menus;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    @JsonIgnore
    public List<GrantedAuthority> getGroupAuthorities() {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
        grantedAuthorities.add(new SimpleGrantedAuthority("GROUP_" + getCode()));
        return grantedAuthorities;
    }

    @JsonIgnore
    public List<GrantedAuthority> getRoleAuthorities() {
        if (ObjectUtil.isNull(getRoles())) {
            return new ArrayList<GrantedAuthority>();
        }
        List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
        for (Role role : getRoles()) {
            grantedAuthorities.addAll(role.getRoleAuthorities());
        }
        return grantedAuthorities;
    }

    public boolean equals(Object obj) {
        if (obj instanceof UserGroup) {
            return getId().equals(((UserGroup) obj).getId());
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return getId() == null ? super.hashCode() : getId().hashCode();
    }

}