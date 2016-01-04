package org.jfantasy.security.bean.databind;


import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.security.bean.User;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class UserDeserializer extends JsonDeserializer<User> {

    @Override
    public User deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        Long value = jp.getValueAsLong();
        if (StringUtil.isBlank(value)) {
            return null;
        }
        return new User(value);
    }

}
