package org.jfantasy.system.bean.databind;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.system.bean.DataDictionaryKey;

import java.io.IOException;

public class DataDictionaryKeyDeserializer extends JsonDeserializer<DataDictionaryKey> {

    @Override
    public DataDictionaryKey deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        String value = jp.getValueAsString();
        if (StringUtil.isBlank(value) || !value.contains(":"))
            return null;
        String[] values = value.split(":");
        return new DataDictionaryKey(values[1],values[0]);
    }
}
