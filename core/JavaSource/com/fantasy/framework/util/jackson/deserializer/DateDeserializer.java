package com.fantasy.framework.util.jackson.deserializer;

import com.fantasy.framework.dao.hibernate.util.ReflectionUtils;
import com.fantasy.framework.error.IgnoreException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.util.Date;

public class DateDeserializer extends JsonDeserializer<Date> {

    final static Log LOG = LogFactory.getLog(DateDeserializer.class);

    @Override
    public Date deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonToken t = jp.getCurrentToken();
        if (t == JsonToken.VALUE_STRING) {
            try {
                return ReflectionUtils.convertStringToObject(jp.getText().trim(), Date.class);
            } catch (Exception e) {
                throw new IgnoreException(e.getMessage(), e);
            }
        }
        LOG.debug("JsonToken = " + t + ",是不能处理的类型！");
        return null;
    }

}
