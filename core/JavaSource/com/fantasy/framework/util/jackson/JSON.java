package com.fantasy.framework.util.jackson;

import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.framework.util.jackson.deserializer.DateDeserializer;
import com.fantasy.framework.util.jackson.serializer.DateSerializer;
import com.fantasy.framework.util.jackson.serializer.StringUnicodeSerializer;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class JSON {

    private static final Log logger = LogFactory.getLog(JSON.class);

    public static final String defaultKey = "default";
    public static final String unicodeKey = "unicode";
    private static final ConcurrentHashMap<String, ObjectMapper> objectMapperCache = new ConcurrentHashMap<String, ObjectMapper>();

    private static final Mirror mirror = new Mirror();

    public static class Mirror {

        public String serialize(Object object) {
            return JSON.serialize(object);
        }

        public Object deserialize(String json) {
            return JSON.deserialize(json);
        }

        public <T> T deserialize(String json, Class<T> classed) {
            return JSON.deserialize(json, classed);
        }

        @SuppressWarnings("unchecked")
        public <T> T[] deserialize(String json, T[] classed) {
            return JSON.deserialize(json, classed);
        }


        @SuppressWarnings("unchecked")
        public <T> T deserialize(String json, TypeReference<T> typeReference) {
            return JSON.deserialize(json, typeReference);
        }

    }

    private static ThreadLocal<String> threadLocal = new ThreadLocal<String>();

    static {
        //默认
        register(defaultKey, new ObjectMapperRegister() {
            @Override
            public void callback(ObjectMapper objectMapper) {
                // 当找不到对应的序列化器时 忽略此字段
                objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
                // 允许非空字段
                objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
                // 允许单引号
                objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
                // 失败在未知属性
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                //使Jackson JSON支持Unicode编码非ASCII字符
                SimpleModule module = new SimpleModule();
//                module.addSerializer(String.class, new StringUnicodeSerializer());
                module.addSerializer(Date.class, new DateSerializer("yyyy-MM-dd HH:mm:ss"));
                module.addDeserializer(Date.class, new DateDeserializer("yyyy-MM-dd HH:mm:ss"));
                objectMapper.registerModule(module);
                //设置null值不参与序列化(字段不被显示)
//                objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

            }
        });
        //将中文转为 Unicode 编码
        register(unicodeKey, new ObjectMapperRegister() {
            @Override
            public void callback(ObjectMapper objectMapper) {
                // 当找不到对应的序列化器时 忽略此字段
                objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
                // 允许非空字段
                objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
                // 允许单引号
                objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
                // 失败在未知属性
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                //使Jackson JSON支持Unicode编码非ASCII字符
                SimpleModule module = new SimpleModule();
                module.addSerializer(String.class, new StringUnicodeSerializer());
                module.addSerializer(Date.class, new DateSerializer("yyyy-MM-dd HH:mm:ss"));
                module.addDeserializer(Date.class, new DateDeserializer("yyyy-MM-dd HH:mm:ss"));
                objectMapper.registerModule(module);
                //设置null值不参与序列化(字段不被显示)
//                objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

            }
        });
    }

    /**
     * 返回 Key 对应的镜像
     *
     * @return Mirror
     */
    public static Mirror set(String key) {
        threadLocal.set(key);
        return mirror;
    }

    public static Mirror unicode() {
        threadLocal.set(unicodeKey);
        return mirror;
    }

    public synchronized static void register(String key, ObjectMapperRegister instanceCallBack) {
        ObjectMapper objectMapper = new ObjectMapper();
        instanceCallBack.callback(objectMapper);
        objectMapperCache.putIfAbsent(key, objectMapper);
    }

    public interface ObjectMapperRegister {

        void callback(ObjectMapper objectMapper);

    }

    public static String serialize(Object object) {
        try {
            String key = threadLocal.get();
            if (StringUtil.isBlank(key)) {
                threadLocal.set(key = defaultKey);
            }
            try {
                return objectMapperCache.get(key).writeValueAsString(object);
            } finally {
                threadLocal.remove();
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return "";
    }

    public static Object deserialize(String json) {
        return deserialize(json, HashMap.class);
    }

    public static ObjectMapper getObjectMapper() {
        String key = threadLocal.get();
        if (StringUtil.isBlank(key)) {
            threadLocal.set(key = defaultKey);
        }
        try {
            return objectMapperCache.get(key);
        } finally {
            threadLocal.remove();
        }
    }

    public static <T> T deserialize(String json, Class<T> classed) {
        try {
            String key = threadLocal.get();
            if (StringUtil.isBlank(key)) {
                threadLocal.set(key = defaultKey);
            }
            try {
                return objectMapperCache.get(key).readValue(json, classed);
            } finally {
                threadLocal.remove();
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] deserialize(String json, T[] classed) {
        try {
            String key = threadLocal.get();
            if (StringUtil.isBlank(key)) {
                threadLocal.set(key = defaultKey);
            }
            try {
                return (T[]) objectMapperCache.get(key).readValue(json, classed.getClass());
            } finally {
                threadLocal.remove();
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }


    @SuppressWarnings("unchecked")
    public static <T> T deserialize(String json, TypeReference<T> typeReference) {
        try {
            String key = threadLocal.get();
            if (StringUtil.isBlank(key)) {
                threadLocal.set(key = defaultKey);
            }
            try {
                return (T) objectMapperCache.get(key).readValue(json, typeReference);
            } finally {
                threadLocal.remove();
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }


}