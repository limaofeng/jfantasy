package org.jfantasy.filestore.converter;

import org.jfantasy.filestore.Image;
import org.jfantasy.framework.jackson.JSON;
import org.jfantasy.framework.util.common.StringUtil;

import javax.persistence.AttributeConverter;

public class ImageConverter implements AttributeConverter<Image, String> {

    @Override
    public String convertToDatabaseColumn(Image attribute) {
        if (attribute == null) {
            return null;
        }
        return JSON.serialize(attribute);
    }

    @Override
    public Image convertToEntityAttribute(String dbData) {
        if (StringUtil.isBlank(dbData)) {
            return null;
        }
        return JSON.deserialize(dbData, Image.class);
    }

}
