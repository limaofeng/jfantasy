package com.fantasy.system.bean.databind;

import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.system.bean.DataDictionaryType;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class DataDictionaryTypeDeserializer extends JsonDeserializer<DataDictionaryType> {

    @Override
    public DataDictionaryType deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String value = jp.getValueAsString();
        if (StringUtil.isBlank(value))
            return null;
        return new DataDictionaryType(value);
    }
}