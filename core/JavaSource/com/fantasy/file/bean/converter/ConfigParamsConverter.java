package com.fantasy.file.bean.converter;

import com.fantasy.file.bean.ConfigParam;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.framework.util.jackson.JSON;
import com.fasterxml.jackson.core.type.TypeReference;

import javax.persistence.AttributeConverter;
import java.util.List;

public class ConfigParamsConverter implements AttributeConverter<List<ConfigParam>, String> {

    @Override
    public String convertToDatabaseColumn(List<ConfigParam> configParams) {
        if (configParams == null) {
            return null;
        }
        return JSON.serialize(configParams);
    }

    @Override
    public List<ConfigParam> convertToEntityAttribute(String dbData) {
        if (StringUtil.isBlank(dbData)) {
            return null;
        }
        return JSON.deserialize(dbData, new TypeReference<List<ConfigParam>>() {
        });
    }

}
