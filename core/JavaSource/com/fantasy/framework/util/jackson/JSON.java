package com.fantasy.framework.util.jackson;

import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.framework.util.jackson.serializer.StringUnicodeSerializer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.ser.CustomSerializerFactory;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class JSON {

    private static final Log logger = LogFactory.getLog(JSON.class);

    public static final String defaultKey = "default";
    public static final String textKey = "text";
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
        //默认将中文转为 Unicode 编码
        register(defaultKey, new ObjectMapperRegister() {
            @Override
            public void callback(ObjectMapper objectMapper) {
                // 当找不到对应的序列化器时 忽略此字段
                objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
                // 允许非空字段
                objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
                // 允许单引号
                objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
                // 失败在未知属性
                objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

                CustomSerializerFactory serializerFactory = new HibernateAwareSerializerFactory();
                serializerFactory.addSpecificMapping(String.class, new StringUnicodeSerializer());

                objectMapper.setSerializerFactory(serializerFactory);

                objectMapper.getDeserializationConfig().withDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
                objectMapper.getSerializationConfig().withDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
            }
        });
        // text 显示时不采用 unicode 中文的方式
        register(textKey, new ObjectMapperRegister() {
            @Override
            public void callback(ObjectMapper objectMapper) {
                // 当找不到对应的序列化器时 忽略此字段
                objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
                // 允许非空字段
                objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
                // 允许单引号
                objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
                // 失败在未知属性
                objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

                objectMapper.getDeserializationConfig().withDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
                objectMapper.getSerializationConfig().withDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
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

    /**
     * 返回 textKey 对应的镜像
     *
     * @return Mirror
     */
    public static Mirror text() {
        threadLocal.set(textKey);
        return mirror;
    }

    public synchronized static void register(String key, ObjectMapperRegister instanceCallBack) {
        if (!objectMapperCache.contains(key)) {
            ObjectMapper objectMapper = new ObjectMapper();
            instanceCallBack.callback(objectMapper);
            objectMapperCache.putIfAbsent(key, objectMapper);
        }
    }

    public interface ObjectMapperRegister {

        public void callback(ObjectMapper objectMapper);

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