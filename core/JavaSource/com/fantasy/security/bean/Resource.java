package com.fantasy.security.bean;

import com.fantasy.framework.dao.BaseBusEntity;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.framework.util.regexp.RegexpUtil;
import com.fantasy.security.bean.enums.ResourceType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 访问规则
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2014年4月23日 下午5:53:06
 */
@Entity
@Table(name = "AUTH_RESOURCE")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "parentResources", "userGroups", "roles"})
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Resource extends BaseBusEntity implements Cloneable {

    private static final long serialVersionUID = -4031735792597359821L;

    @Id
    @Column(name = "ID", nullable = false, insertable = true, updatable = true, precision = 22, scale = 0)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;

    /**
     * 资源名称
     */
    @Column(name = "NAME")
    private String name;
    /**
     * 资源值
     */
    @Column(name = "VALUE")
    private String value;
    /**
     * 资源类型
     */
    @Column(name = "TYPE", length = 20)
    @Enumerated(EnumType.STRING)
    private ResourceType type;
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

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "AUTH_RELATION_RESOURCE", joinColumns = @JoinColumn(name = "SUB_RESOURCE_ID"), inverseJoinColumns = @JoinColumn(name = "RESOURCE_ID"))
    private List<Resource> parentResources;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "AUTH_RELATION_RESOURCE", joinColumns = @JoinColumn(name = "RESOURCE_ID"), inverseJoinColumns = @JoinColumn(name = "SUB_RESOURCE_ID"))
    private List<Resource> subResources;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "AUTH_USERGROUP_RESOURCE", joinColumns = @JoinColumn(name = "RESOURCE_ID"), inverseJoinColumns = @JoinColumn(name = "USERGROUP_ID"))
    public List<UserGroup> userGroups;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "AUTH_ROLE_RESOURCE", joinColumns = @JoinColumn(name = "RESOURCE_ID"), inverseJoinColumns = @JoinColumn(name = "ROLE_CODE"))
    private List<Role> roles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setType(ResourceType type) {
        this.type = type;
    }

    public ResourceType getType() {
        return this.type;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
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

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @JsonIgnore
    public Boolean isEnabled() {
        return ObjectUtil.defaultValue(this.enabled, Boolean.FALSE);
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

    public List<Resource> getParentResources() {
        return parentResources;
    }

    public void setParentResources(List<Resource> parentResources) {
        this.parentResources = parentResources;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @JsonProperty("children")
    public List<Resource> getSubResources() {
        return subResources;
    }

    public void setSubResources(List<Resource> subResources) {
        this.subResources = subResources;
    }

    public void addResource(Resource resource) {
        if (ObjectUtil.isNull(getSubResources())) {
            setSubResources(new ArrayList<Resource>());
        }
        getSubResources().add(resource);
    }

    @JsonIgnore
    public String getRoleAuthorities() {
        AtomicReference<StringBuffer> roleAuthorities = new AtomicReference<StringBuffer>(new StringBuffer());
        for (Role role : (getRoles() == null ? new ArrayList<Role>() : getRoles())) {
            if (!role.isEnabled()) {
                continue;
            }
            List<GrantedAuthority> authorities = role.getRoleAuthorities();
            for (GrantedAuthority authority : authorities) {
                roleAuthorities.get().append(authority.getAuthority()).append(",");
            }
        }
        return RegexpUtil.replace(roleAuthorities.get().toString(), ",$", "");
    }

    @JsonIgnore
    public String getAuthorities() {
        AtomicReference<StringBuffer> authorities = new AtomicReference<StringBuffer>(new StringBuffer());
        String roleAuthorities = getRoleAuthorities();
        if (StringUtil.isNotBlank(roleAuthorities)) {
            authorities.get().append(roleAuthorities).append(",");
        }
        String userGroupAuthorities = getUserGroupAuthorities();
        if (StringUtil.isNotBlank(userGroupAuthorities)) {
            authorities.get().append(userGroupAuthorities).append(",");
        }
        return RegexpUtil.replace(authorities.get().toString(), ",$", "");
    }

    @JsonIgnore
    public String getUserGroupAuthorities() {
        AtomicReference<StringBuffer> userGroupAuthorities = new AtomicReference<StringBuffer>(new StringBuffer());
        for (UserGroup userGroup : (getUserGroups() == null ? new ArrayList<UserGroup>() : getUserGroups())) {
            if (userGroup.isEnabled()) {
                List<GrantedAuthority> authorities = userGroup.getGroupAuthorities();
                for (GrantedAuthority authority : authorities) {
                    userGroupAuthorities.get().append(authority.getAuthority()).append(",");
                }
            }
        }
        return RegexpUtil.replace(userGroupAuthorities.get().toString(), ",$", "");
    }

    @JsonIgnore
    public GrantedAuthority getUrlAuthoritie() {
        return new SimpleGrantedAuthority("URL_" + getValue());
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return ObjectUtil.clone(this);
    }

}