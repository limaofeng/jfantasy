package org.jfantasy.security.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.security.bean.enums.ResourceType;

import javax.persistence.*;
import java.util.List;

/**
 * 资源
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2014年4月23日 下午5:53:06
 */
@Entity
@Table(name = "AUTH_RESOURCE")
@TableGenerator(name = "resource_gen", table = "sys_sequence",pkColumnName = "gen_name",pkColumnValue = "auth_resource:id",valueColumnName = "gen_value")
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler", "parentResources", "userGroups", "roles"})
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Resource extends BaseBusEntity implements Cloneable {

    private static final long serialVersionUID = -4031735792597359821L;

    @Id
    @Column(name = "ID", nullable = false, precision = 22, scale = 0)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "resource_gen")
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
    @Lob
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

    @OneToMany(mappedBy = "resource", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    private List<Permission> permissions;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Resource)) return false;
        Resource resource = (Resource) o;
        return id.equals(resource.id);
    }

    @Override
    public int hashCode() {
        return getId() == null ? super.hashCode() : getId().hashCode();
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return ObjectUtil.clone(this);
    }

}