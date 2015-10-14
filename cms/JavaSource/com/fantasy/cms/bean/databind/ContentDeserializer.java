package com.fantasy.cms.bean.databind;

import com.fantasy.cms.bean.Content;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class ContentDeserializer extends JsonDeserializer<Content> {

    @Override
    public Content deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        return new Content(jp.getValueAsString());
    }

}