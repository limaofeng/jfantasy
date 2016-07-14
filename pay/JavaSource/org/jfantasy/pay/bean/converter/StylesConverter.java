package org.jfantasy.pay.bean.converter;

import org.jfantasy.framework.jackson.JSON;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.pay.bean.Styles;

import javax.persistence.AttributeConverter;

/**
 * 设计样式
 */
public class StylesConverter implements AttributeConverter<Styles, String> {

    @Override
    public String convertToDatabaseColumn(Styles attribute) {
        if (attribute == null) {
            return null;
        }
        return JSON.serialize(attribute);
    }

    @Override
    public Styles convertToEntityAttribute(String dbData) {
        if (StringUtil.isBlank(dbData)) {
            return null;
        }
        return JSON.deserialize(dbData, Styles.class);
    }

}
