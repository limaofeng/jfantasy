package org.jfantasy.common.bean.converter;

import org.jfantasy.common.bean.Area;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.framework.jackson.JSON;

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