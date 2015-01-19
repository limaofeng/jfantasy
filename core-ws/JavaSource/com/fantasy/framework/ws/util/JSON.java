package com.fantasy.framework.ws.util;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class JSON {

    private static final Log logger = LogFactory.getLog(JSON.class);

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        // 当找不到对应的序列化器时 忽略此字段
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        // 允许非空字段
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        // 允许单引号
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        // 失败在未知属性
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        BeanSerializerFactory serializerFactory = BeanSerializerFactory.instance;

        objectMapper.setSerializerFactory(serializerFactory);

        objectMapper.getDeserializationConfig().with(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        objectMapper.getSerializationConfig().with(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

    }

    @SuppressWarnings("unchecked")
    public static <T> T[] deserialize(String json, T[] classed) {
        try {
            return (T[]) objectMapper.readValue(json, classed.getClass());
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

}
