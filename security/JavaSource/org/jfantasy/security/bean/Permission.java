package org.jfantasy.security.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.util.common.ObjectUtil;

import javax.persistence.*;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * 权限配置信息
 */
@Entity
@Table(name = "AUTH_PERMISSION")
@TableGenerator(name = "permission_gen", table = "sys_sequence",pkColumnName = "gen_name",pkColumnValue = "auth_permission:id",valueColumnName = "gen_value")
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler", "userGroups", "roles"})
public class Permission extends BaseBusEntity implements Cloneable {

    private static final long serialVersionUID = 2224908963065749499L;
    @Id
    @Column(name = "ID", nullable = false, precision = 22)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "permission_gen")
    private Long id;
    /**
     * 权限名称
     */
    @Column(name = "NAME")
    private String name;
    /**
     * 配置规则
     */
    @Lob
    @Column(name = "VALUE")
    private String value;
    /**
     * 是否启用
     */
    @Column(name = "ENABLED")
    private Boolean enabled;
    /**
     * 资源描述
     */
    @Column(name = "DESCRIPTION")
    private String description;
    /**
     * 对应的资源
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RESOURCE_ID", foreignKey = @ForeignKey(name = "FK_AUTH_PERMISSION_RESOURCE_PID"))
    private Resource resource;
    /**
     * 用户组
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "AUTH_USERGROUP_PERMISSION", joinColumns = @JoinColumn(name = "PERMISSION_ID"), inverseJoinColumns = @JoinColumn(name = "USERGROUP_ID"))
    public List<UserGroup> userGroups;
    /**
     * 角色
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "AUTH_ROLE_PERMISSION", joinColumns = @JoinColumn(name = "PERMISSION_ID"), inverseJoinColumns = @JoinColumn(name = "ROLE_CODE"))
    private List<Role> roles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @JsonIgnore
    public List<Role> getRoles() {
        return this.roles;
    }

    @JsonIgnore
    public List<UserGroup> getUserGroups() {
        return userGroups;
    }

    public void setUserGroups(List<UserGroup> userGroups) {
        this.userGroups = userGroups;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    @JsonIgnore
    public Boolean isEnabled() {
        return ObjectUtil.defaultValue(this.enabled, Boolean.FALSE);
    }

    @Transient
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

}
