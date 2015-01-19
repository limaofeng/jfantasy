package com.fantasy.framework.util.jackson.deserializer;

import com.fantasy.framework.util.common.DateUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.util.Date;

public class DateDeserializer extends JsonDeserializer<Date> {

    private final static Log LOG = LogFactory.getLog(DateDeserializer.class);

    private String dateFormat;

    public DateDeserializer(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    @Override
    public Date deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonToken t = jp.getCurrentToken();
        if (t == JsonToken.VALUE_STRING) {
            return DateUtil.parse(jp.getText().trim(), this.dateFormat);
        }
        LOG.debug("JsonToken = " + t + ",是不能处理的类型！");
        return null;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

}
