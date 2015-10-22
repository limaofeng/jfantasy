package com.fantasy.attr.framework;

import com.fantasy.attr.storage.bean.AttributeValue;
import com.fantasy.attr.storage.bean.AttributeVersion;

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
    AttributeVersion getVersion();

    void setVersion(AttributeVersion version);

    /**
     * 获取动态属性
     *
     * @return List<AttributeValue>
     */
    List<AttributeValue> getAttributeValues();

    /**
     * 设置动态属性
     *
     * @param attributeValues List<AttributeValue>
     */
    void setAttributeValues(List<AttributeValue> attributeValues);

    void setAttributeValueStore(String json);

    String getAttributeValueStore();

    DynaBean getTarget();

    void setTarget(DynaBean target);

}
