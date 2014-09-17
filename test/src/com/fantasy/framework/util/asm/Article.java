package com.fantasy.framework.util.asm;


import com.fantasy.attr.bean.AttributeValue;
import com.fantasy.framework.lucene.annotations.Indexed;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Indexed
@Entity
@Table(name = "CMS_ARTICLE")
public class Article {

    @Id
    @Column(name = "ID", nullable = false, insertable = true, updatable = true, precision = 22, scale = 0)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    @JoinColumns(value = {@JoinColumn(name = "TARGET_ID", referencedColumnName = "ID"), @JoinColumn(name = "VERSION_ID", referencedColumnName = "VERSION_ID")})
    private List<AttributeValue> attributeValues;

    public List<AttributeValue> getAttributeValues() {
        return attributeValues;
    }

    public void setAttributeValues(List<AttributeValue> attributeValues) {
        this.attributeValues = attributeValues;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private String test;

    public void setTest(String test){
        AttributeValueUtil.saveValue(attributeValues,"test",test);
    }

}
