package org.jfantasy.oauth.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.security.bean.Role;

import javax.persistence.*;
import java.util.List;

/**
 * 应用
 */
@Entity
@Table(name = "OAUTH_APP")
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler"})
public class Application extends BaseBusEntity {

    private static final long serialVersionUID = 5943657998408920009L;

    @Id
    @Column(name = "ID", updatable = false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "test:id")
    @TableGenerator(name = "test:id", table = "sys_sequence",pkColumnName = "gen_name",valueColumnName = "gen_value")
    private Long id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "DESCRIPTION")
    private String description;
    @OneToMany(mappedBy = "application", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    private List<ApiKey> apiKeys;
    @Column(name = "ENABLED", nullable = false)
    private Boolean enabled;
    /**
     * 用户对应的角色
     */
    @ManyToMany(targetEntity = Role.class, fetch = FetchType.LAZY)
    @JoinTable(name = "AUTH_ROLE_APP", joinColumns = @JoinColumn(name = "APP_ID"), inverseJoinColumns = @JoinColumn(name = "ROLE_CODE"), foreignKey = @ForeignKey(name = "FK_ROLE_APP_AID"))
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private List<Role> roles;

    public Application(){}

    public Application(Long id) {
        this.id = id;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ApiKey> getApiKeys() {
        return apiKeys;
    }

    public void setApiKeys(List<ApiKey> apiKeys) {
        this.apiKeys = apiKeys;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
