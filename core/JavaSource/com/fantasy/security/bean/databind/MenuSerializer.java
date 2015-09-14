package com.fantasy.security.bean.databind;

import com.fantasy.security.bean.Menu;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class MenuSerializer extends JsonSerializer<Menu> {
    @Override
    public void serialize(Menu menu, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeString(menu.getId() != null ? menu.getId().toString() : "");
    }
}