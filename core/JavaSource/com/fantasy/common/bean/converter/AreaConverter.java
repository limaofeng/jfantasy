package com.fantasy.common.bean.converter;

import com.fantasy.common.bean.Area;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.framework.util.jackson.JSON;

import javax.persistence.AttributeConverter;

public class AreaConverter implements AttributeConverter<Area, String> {

    @Override
    public String convertToDatabaseColumn(Area attribute) {
        if (attribute == null) {
            return null;
        }
        return JSON.serialize(attribute);
    }

    @Override
    public Area convertToEntityAttribute(String dbData) {
        if (StringUtil.isBlank(dbData)) {
            return null;
        }
        return JSON.deserialize(dbData, Area.class);
    }
}