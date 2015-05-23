package com.fantasy.attr.storage.bean;

import com.fantasy.framework.dao.BaseBusEntity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "CUSTOM_BEAN")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CustomBean extends BaseBusEntity {

    @Id
    @Column(name = "ID", nullable = false, insertable = true, updatable = true, precision = 22, scale = 0)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    /**
     * 自定义 bean 的名称
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DEFINITION_ID")
    private CustomBeanDefinition definition;
    /**
     * 对应的版本
     */
    @Column(name = "VERSION_ID")
    private Long version;
    /**
     * 属性
     */
    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumns(value = {@JoinColumn(name = "TARGET_ID", referencedColumnName = "ID"), @JoinColumn(name = "VERSION_ID", referencedColumnName = "VERSION_ID")})
    private List<AttributeValue> attributeValues;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<AttributeValue> getAttributeValues() {
        return attributeValues;
    }

    public void setAttributeValues(List<AttributeValue> attributeValues) {
        this.attributeValues = attributeValues;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public CustomBeanDefinition getDefinition() {
        return definition;
    }

    public void setDefinition(CustomBeanDefinition definition) {
        this.definition = definition;
    }
}
