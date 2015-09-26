package com.fantasy.file.bean.converter;

import com.fantasy.file.bean.FileManagerConfig;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.framework.util.jackson.JSON;
import com.fasterxml.jackson.core.type.TypeReference;

import javax.persistence.AttributeConverter;
import java.util.Collections;
import java.util.List;

public class ConfigParamsConverter implements AttributeConverter<List<FileManagerConfig.ConfigParam>, String> {

    @Override
    public String convertToDatabaseColumn(List<FileManagerConfig.ConfigParam> configParams) {
        if (configParams == null) {
            return null;
        }
        return JSON.serialize(configParams);
    }

    @Override
    public List<FileManagerConfig.ConfigParam> convertToEntityAttribute(String dbData) {
        return StringUtil.isBlank(dbData) ? Collections.<FileManagerConfig.ConfigParam>emptyList() : JSON.deserialize(dbData, new TypeReference<List<FileManagerConfig.ConfigParam>>() {
        });
    }

}
