package org.jfantasy.attr.storage;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.jfantasy.attr.framework.DynaBean;
import org.jfantasy.attr.storage.bean.AttributeValue;
import org.jfantasy.attr.storage.bean.AttributeVersion;
import org.jfantasy.framework.dao.BaseBusEntity;

import java.util.List;

@MappedSuperclass
public abstract class BaseDynaBean extends BaseBusEntity implements DynaBean {

    @JsonIgnore
    @Transient
    private DynaBean target;

    /**
     * 数据版本
     */
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VERSION_ID")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private AttributeVersion version;
    /**
     * 动态属性集合。
     */
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumns(value = {@JoinColumn(name = "TARGET_ID", referencedColumnName = "ID"), @JoinColumn(name = "VERSION_ID", referencedColumnName = "VERSION_ID")})
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private List<AttributeValue> attributeValues;
    /**
     * 字段缓存
     */
    @ApiModelProperty(hidden = true)
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

    public DynaBean getTarget() {
        return target;
    }

    public void setTarget(DynaBean target) {
        this.target = target;
    }
}
