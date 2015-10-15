package com.fantasy.mall.goods.bean.converter;


import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.framework.util.jackson.JSON;
import com.fantasy.mall.goods.bean.GoodsParameterValue;

import javax.persistence.AttributeConverter;

public class GoodsParameterValuesConverter implements AttributeConverter<GoodsParameterValue[], String> {

    @Override
    public String convertToDatabaseColumn(GoodsParameterValue[] attribute) {
        if (attribute == null) {
            return null;
        }
        return JSON.serialize(attribute);
    }

    @Override
    public GoodsParameterValue[] convertToEntityAttribute(String dbData) {
        if (StringUtil.isBlank(dbData)) {
            return null;
        }
        return JSON.deserialize(dbData, GoodsParameterValue[].class);
    }
}
