package com.fantasy.attr.storage.bean;

import com.fantasy.framework.dao.BaseBusEntity;
import com.fantasy.framework.util.common.StringUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.IOException;

/**
 * 属性值表
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2012-11-4 下午06:10:12
 */
@Entity
@Table(name = "ATTR_ATTRIBUTE_VALUE", uniqueConstraints = {@UniqueConstraint(columnNames = {"VERSION_ID", "ATTRIBUTE_ID", "TARGET_ID"})})
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "version"})
public class AttributeValue extends BaseBusEntity {

    private static final long serialVersionUID = 5155306149647104462L;

    @Id
    @Column(name = "ID", updatable = false)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    /**
     * 属性对象
     */
    @JoinColumn(name = "ATTRIBUTE_ID", updatable = false, foreignKey = @ForeignKey(name = "FK_ATTRIBUTE_VALUE_ATTRIBUTE"))
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = AttributeSerialize.class)
    @JsonDeserialize(using = AttributeDeserialize.class)
    private Attribute attribute;
    /**
     * 数据版本
     */
    @JoinColumn(name = "VERSION_ID", updatable = false, foreignKey = @ForeignKey(name = "FK_ATTRIBUTE_VALUE_VERSION"))
    @ManyToOne(fetch = FetchType.LAZY)
    private AttributeVersion version;
    /**
     * 关联对象对应的id
     */
    @Column(name = "TARGET_ID", updatable = false)
    private Long targetId;
    /**
     * 属性值
     */
    @Lob
    @Column(name = "VALUE")
    private String value;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public void setAttribute(Attribute attribute) {
        this.attribute = attribute;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public AttributeVersion getVersion() {
        return version;
    }

    public void setVersion(AttributeVersion version) {
        this.version = version;
    }

    public static class AttributeSerialize extends JsonSerializer<Attribute> {

        @Override
        public void serialize(Attribute attribute, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            if (attribute == null) {
                jgen.writeNull();
            } else {
                jgen.writeString(attribute.getCode());
            }
        }

    }

    public static class AttributeDeserialize extends JsonDeserializer<Attribute> {

        @Override
        public Attribute deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            String code = jp.getValueAsString();
            return StringUtil.isNotBlank(code) ? new Attribute(code) : null;
        }

    }

    @Override
    public String toString() {
        return "AttributeValue{" +
                "attribute=" + attribute +
                ", value='" + value + '\'' +
                ", targetId=" + targetId +
                '}';
    }
}
