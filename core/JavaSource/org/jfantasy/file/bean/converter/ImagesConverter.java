package org.jfantasy.file.bean.converter;

import org.jfantasy.file.bean.Image;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.framework.util.jackson.JSON;

import javax.persistence.AttributeConverter;

public class ImagesConverter implements AttributeConverter<Image[], String> {

    @Override
    public String convertToDatabaseColumn(Image[] attribute) {
        if (attribute == null) {
            return null;
        }
        return JSON.serialize(attribute);
    }

    @Override
    public Image[] convertToEntityAttribute(String dbData) {
        if (StringUtil.isBlank(dbData)) {
            return null;
        }
        return JSON.deserialize(dbData, Image[].class);
    }
}
