package com.fantasy.attr;

import com.fantasy.attr.bean.AttributeValue;
import com.fantasy.attr.bean.AttributeVersion;

import java.util.List;

/**
 * 动态Bean接口
 */
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

}
