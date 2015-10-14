package com.fantasy.file.bean.converter;

import com.fantasy.file.bean.FileDetail;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.framework.util.jackson.JSON;

import javax.persistence.AttributeConverter;

public class FileDetailConverter implements AttributeConverter<FileDetail, String> {

    @Override
    public String convertToDatabaseColumn(FileDetail attribute) {
        if (attribute == null) {
            return null;
        }
        return JSON.serialize(attribute);
    }

    @Override
    public FileDetail convertToEntityAttribute(String dbData) {
        if (StringUtil.isBlank(dbData)) {
            return null;
        }
        return JSON.deserialize(dbData, FileDetail.class);
    }
}
