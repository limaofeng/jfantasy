package org.jfantasy.common.converter;

import org.jfantasy.common.Area;
import org.jfantasy.framework.jackson.JSON;
import org.jfantasy.framework.util.common.StringUtil;

import javax.persistence.AttributeConverter;

public class AreaConverter implements AttributeConverter<Area, String> {

    public AreaConverter() {
    }

    public String convertToDatabaseColumn(Area attribute) {
        return attribute == null ? null : JSON.serialize(attribute);
    }

    public Area convertToEntityAttribute(String dbData) {
        return StringUtil.isBlank(dbData) ? null : JSON.deserialize(dbData, Area.class);
    }

}
