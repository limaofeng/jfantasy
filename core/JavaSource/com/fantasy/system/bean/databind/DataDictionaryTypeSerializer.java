package com.fantasy.system.bean.databind;

import com.fantasy.system.bean.DataDictionaryType;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class DataDictionaryTypeSerializer extends JsonSerializer<DataDictionaryType> {

    @Override
    public void serialize(DataDictionaryType ddt, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeString(ddt.getCode());
    }

}