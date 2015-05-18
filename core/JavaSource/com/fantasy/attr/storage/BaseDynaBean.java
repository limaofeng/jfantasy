package com.fantasy.attr.storage;

import com.fantasy.attr.framework.DynaBean;
import com.fantasy.attr.storage.bean.AttributeValue;
import com.fantasy.attr.storage.bean.AttributeVersion;
import com.fantasy.framework.dao.BaseBusEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.List;

@MappedSuperclass
public abstract class BaseDynaBean extends BaseBusEntity implements DynaBean {
    /**
     * 数据版本
     */
    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VERSION_ID")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private AttributeVersion version;
    /**
     * 动态属性集合。
     */
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumns(value = {@JoinColumn(name = "TARGET_ID", referencedColumnName = "ID"), @JoinColumn(name = "VERSION_ID", referencedColumnName = "VERSION_ID")})
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private List<AttributeValue> attributeValues;
    /**
     * 字段缓存
     */
    @Lob
    @JsonIgnore
    @Column(name = "ATTRIBUTE_VALUE_STORE")
    private String attributeValueStore;

    public void setVersion(AttributeVersion version) {
        this.version = version;
    }

    public List<AttributeValue> getAttributeValues() {
        return attributeValues;
    }

    public void setAttributeValues(List<AttributeValue> attributeValues) {
        this.attributeValues = attributeValues;
    }

    @Override
    public void setAttributeValueStore(String json) {
        this.attributeValueStore = json;
    }

    @Override
    public String getAttributeValueStore() {
        return this.attributeValueStore;
    }

    @Override
    public AttributeVersion getVersion() {
        return version;
    }
}
