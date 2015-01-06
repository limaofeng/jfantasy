package com.fantasy.security.bean;

import com.fantasy.framework.dao.BaseBusEntity;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 组织维度
 */
@Entity
@Table(name = "AUTH_ORG_DIMENSION")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class OrgDimension extends BaseBusEntity {

    /**
     * 维度Id
     */
    @Column(name = "CODE")
    private String id;
    /**
     * 维度名称
     */
    @Column(name = "NAME")
    private String name;
    /**
     * 维度描述
     */
    @Column(name = "DESCRIPTION")
    private String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
}
