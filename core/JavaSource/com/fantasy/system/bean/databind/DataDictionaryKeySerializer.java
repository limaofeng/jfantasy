package com.fantasy.system.bean.databind;

import com.fantasy.framework.error.IgnoreException;
import com.fantasy.system.bean.DataDictionaryKey;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class DataDictionaryKeySerializer extends JsonSerializer<DataDictionaryKey> {

    @Override
    public void serialize(DataDictionaryKey dataDictionaryKey, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        try {
            if (dataDictionaryKey == null) {
                jgen.writeString("");
            } else {
                jgen.writeString(dataDictionaryKey.getType() + ":" + dataDictionaryKey.getCode());
            }
        } catch (Exception e) {
            jgen.writeString("");
            throw new IgnoreException(e.getMessage(), e);
        }
    }

}
