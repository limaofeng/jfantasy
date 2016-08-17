package org.jfantasy.filestore.converter;

import org.jfantasy.filestore.File;
import org.jfantasy.framework.jackson.JSON;
import org.jfantasy.framework.util.common.StringUtil;

import javax.persistence.AttributeConverter;

public class FileConverter implements AttributeConverter<File, String> {

    @Override
    public String convertToDatabaseColumn(File attribute) {
        if (attribute == null) {
            return null;
        }
        return JSON.serialize(attribute);
    }

    @Override
    public File convertToEntityAttribute(String dbData) {
        if (StringUtil.isBlank(dbData)) {
            return null;
        }
        return JSON.deserialize(dbData, File.class);
    }
}
