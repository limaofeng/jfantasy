package com.fantasy.security.bean;

import com.fantasy.framework.dao.BaseBusEntity;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.framework.util.jackson.JSON;
import com.fantasy.security.bean.enums.ResourceType;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 资源
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2014年4月23日 下午5:53:06
 */
@ApiModel(value = "资源", description = "每个访问链接、方法、类，都可以抽象为一个资源")
@Entity
@Table(name = "AUTH_RESOURCE")
@JsonFilter(JSON.CUSTOM_FILTER)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "parentResources", "userGroups", "roles"})
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Resource extends BaseBusEntity implements Cloneable {

    private static final long serialVersionUID = -4031735792597359821L;

    @ApiModelProperty("资源ID")
    @Id
    @Column(name = "ID", nullable = false, insertable = true, updatable = true, precision = 22, scale = 0)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    /**
     * 资源名称
     */
    @ApiModelProperty("资源名称")
    @Column(name = "NAME")
    private String name;
    /**
     * 资源值
     */
    @ApiModelProperty("资源值")
    @Column(name = "VALUE")
    private String value;
    /**
     * 资源类型
     */
    @ApiModelProperty("资源类型")
    @Column(name = "TYPE", length = 20)
    @Enumerated(EnumType.STRING)
    private ResourceType type;
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

    @Override
    public Object clone() throws CloneNotSupportedException {
        return ObjectUtil.clone(this);
    }
}