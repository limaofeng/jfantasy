package com.fantasy.system.bean.databind;

import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.system.bean.DataDictionaryKey;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class DataDictionaryKeyDeserializer extends JsonDeserializer<DataDictionaryKey> {

    @Override
    public DataDictionaryKey deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String value = jp.getValueAsString();
        if (StringUtil.isBlank(value) || value.contains(":"))
            return null;
        String[] values = value.split(":");
        return new DataDictionaryKey(values[1],values[0]);
    }
}
