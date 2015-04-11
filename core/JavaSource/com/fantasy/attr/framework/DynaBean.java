package com.fantasy.attr.framework;

import com.fantasy.attr.storage.bean.AttributeValue;
import com.fantasy.attr.storage.bean.AttributeVersion;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * 动态Bean接口
 */
@JsonIgnoreProperties({"attributeValueStore", "version", "attributeValues"})
public interface DynaBean {

    /**
     * 获取接口版本定义
     *
     * @return AttributeVersion
     */
    public AttributeVersion getVersion();

    public void setVersion(AttributeVersion version);

    /**
     * 获取动态属性
     *
     * @return List<AttributeValue>
     */
    public List<AttributeValue> getAttributeValues();

    /**
     * 设置动态属性
     *
     * @param attributeValues List<AttributeValue>
     */
    public void setAttributeValues(List<AttributeValue> attributeValues);

    public void setAttributeValueStore(String json);

    public String getAttributeValueStore();

}
