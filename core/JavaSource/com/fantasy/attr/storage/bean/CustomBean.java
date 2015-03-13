package com.fantasy.attr.storage.bean;

import com.fantasy.attr.framework.DynaBean;
import com.fantasy.attr.framework.query.DynaBeanEntityPersister;
import com.fantasy.framework.dao.BaseBusEntity;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Persister;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "CUSTOM_BEAN")
@Persister(impl = DynaBeanEntityPersister.class)
public class CustomBean extends BaseBusEntity implements DynaBean {

    @Id
    @Column(name = "ID", nullable = false, insertable = true, updatable = true, precision = 22, scale = 0)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;

    /**
     * 数据版本
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VERSION_ID", foreignKey = @ForeignKey(name = "FK_CMS_ARTICLE_VERSION"))
    private AttributeVersion version;

    /**
     * 动态属性集合。
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

    @Override
    public AttributeVersion getVersion() {
        return version;
    }

    @Override
    public void setVersion(AttributeVersion version) {
        this.version = version;
    }

    @Override
    public List<AttributeValue> getAttributeValues() {
        return attributeValues;
    }

    @Override
    public void setAttributeValues(List<AttributeValue> attributeValues) {
        this.attributeValues = attributeValues;
    }

}
