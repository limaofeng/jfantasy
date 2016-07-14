package org.jfantasy.security.bean.databind;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.security.bean.Menu;

import java.io.IOException;

public class MenuDeserializer extends JsonDeserializer<Menu> {

    @Override
    public Menu deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        if (StringUtil.isBlank(jp.getValueAsString())) {
            return null;
        }
        return new Menu(jp.getValueAsString());
    }

}