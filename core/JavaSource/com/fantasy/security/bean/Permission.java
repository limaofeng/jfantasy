package com.fantasy.security.bean;

import com.fantasy.framework.dao.BaseBusEntity;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.framework.util.jackson.JSON;
import com.fantasy.security.bean.enums.PermissionType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@ApiModel(value = "权限配置", description = "权限配置信息")
@Entity
@Table(name = "AUTH_PERMISSION")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "userGroups", "roles"})
public class Permission extends BaseBusEntity implements Cloneable {

    @ApiModelProperty("权限ID")
    @Id
    @Column(name = "ID", nullable = false, insertable = true, updatable = true, precision = 22, scale = 0)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    /**
     * 权限名称
     */
    @ApiModelProperty("权限名称")
    @Column(name = "NAME")
    private String name;
    /**
     * 权限类型
     */
    @ApiModelProperty("权限规则类型")
    @Column(name = "TYPE", length = 20)
    @Enumerated(EnumType.STRING)
    private PermissionType type;
    /**
     * 配置规则
     */
    @ApiModelProperty(hidden = true)
    @Column(name = "VALUE")
    private String value;
    /**
     * 是否启用
     */
    @ApiModelProperty("是否启用")
    @Column(name = "ENABLED")
    private Boolean enabled;
    /**
     * 资源描述
     */
    @ApiModelProperty("资源描述")
    @Column(name = "DESCRIPTION")
    private String description;
    /**
     * 对应的资源
     */
    @ApiModelProperty(value = "对应的资源")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RESOURCE_ID", foreignKey = @ForeignKey(name = "FK_AUTH_PERMISSION_RESOURCE_PID"))
    private Resource resource;
    /**
     * 用户组
     */
    @ApiModelProperty(hidden = true)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "AUTH_USERGROUP_RESOURCE", joinColumns = @JoinColumn(name = "RESOURCE_ID"), inverseJoinColumns = @JoinColumn(name = "USERGROUP_ID"))
    public List<UserGroup> userGroups;
    /**
     * 角色
     */
    @ApiModelProperty(hidden = true)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "AUTH_ROLE_RESOURCE", joinColumns = @JoinColumn(name = "RESOURCE_ID"), inverseJoinColumns = @JoinColumn(name = "ROLE_CODE"))
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

    public PermissionType getType() {
        return type;
    }

    public void setType(PermissionType type) {
        this.type = type;
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

    @ApiModelProperty("权限规则类型")
    public PermissionRule[] getPermissionRules() {
        if (StringUtil.isBlank(this.getValue())) {
            return new PermissionRule[0];
        }
        return JSON.deserialize(this.getValue(), PermissionRule[].class);
    }

    @JsonIgnore
    @Transient
    @ApiModelProperty(hidden = true)
    public RequestMatcher getRequestMatcher() {
        PermissionRule[] permissionRules = getPermissionRules();
        if (this.getType() == PermissionType.and || this.getType() == PermissionType.or) {
            if (permissionRules == null || permissionRules.length == 0) {
                return null;
            }
            return new AndRequestMatcher(getRequestMatcher());
        } else {
            PermissionRule permissionRule = permissionRules[0];
            if (permissionRule == null) {
                return null;
            }
            return permissionRule.getRequestMatcher();
        }
    }

    @ApiModelProperty(hidden = true)
    public static List<RequestMatcher> getRequestMatcher(PermissionRule[] permissionRules) {
        List<RequestMatcher> requestMatchers = new ArrayList<RequestMatcher>();
        return null;
    }

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    @Transient
    public List<ConfigAttribute> getAuthorities() {
        List<ConfigAttribute> configAttributes = new ArrayList<ConfigAttribute>();
        for (UserGroup userGroup : getUserGroups() == null ? new ArrayList<UserGroup>() : getUserGroups()) {
            if (userGroup.isEnabled()) {
                for (GrantedAuthority authority : userGroup.getGroupAuthorities()) {
                    configAttributes.add(new SecurityConfig(authority.getAuthority()));
                }
            }
        }
        for (Role role : getRoles() == null ? new ArrayList<Role>() : getRoles()) {
            if (role.isEnabled()) {
                for (GrantedAuthority authority : role.getRoleAuthorities()) {
                    configAttributes.add(new SecurityConfig(authority.getAuthority()));
                }
            }
        }
        return configAttributes;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return ObjectUtil.clone(this);
    }

}
