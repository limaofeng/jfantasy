package org.jfantasy.framework.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfantasy.framework.jackson.deserializer.DateDeserializer;
import org.jfantasy.framework.jackson.serializer.DateSerializer;
import org.jfantasy.framework.jackson.serializer.StringUnicodeSerializer;
import org.jfantasy.framework.util.common.ClassUtil;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

public class JSON {

    private static final Log LOG = LogFactory.getLog(JSON.class);

    public static final String DEFAULT_KEY = "default";
    private static final String UNICODE_KEY = "unicode";
    private static final ConcurrentHashMap<String, ObjectMapper> OBJECT_MAPPER_CACHE = new ConcurrentHashMap<>();

    static {
        //默认
        register(DEFAULT_KEY, new ObjectMapper()
                .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)//为空的字段不序列化
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)// 当找不到对应的序列化器时 忽略此字段
                .enable(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES)// 允许非空字段
                .enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES)// 允许单引号
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)// 设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
                .registerModule(new SimpleModule()// 默认日期转换方式
                        .addSerializer(Date.class, new DateSerializer("yyyy-MM-dd HH:mm:ss"))
                        .addDeserializer(Date.class, new DateDeserializer())));
        //将中文转为 Unicode 编码
        register(UNICODE_KEY, new ObjectMapper().disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)// 当找不到对应的序列化器时 忽略此字段
                .enable(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES)// 允许非空字段
                .enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES)// 允许单引号
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)// 失败在未知属性
                .registerModule(new SimpleModule()//使Jackson JSON支持Unicode编码非ASCII字符
                        .addSerializer(String.class, new StringUnicodeSerializer())
                        .addSerializer(Date.class, new DateSerializer("yyyy-MM-dd HH:mm:ss"))
                        .addDeserializer(Date.class, new DateDeserializer())));
    }

    public synchronized static void register(String key, ObjectMapper objectMapper) {
        OBJECT_MAPPER_CACHE.putIfAbsent(key, objectMapper);
    }

    public static String serialize(Object object, String... ignoreProperties) {
        if (object == null) {
            return null;
        }
        try {
            ThreadJacksonMixInHolder mixInHolder = ThreadJacksonMixInHolder.getMixInHolder();
            return mixInHolder.getObjectWriter().writeValueAsString(object);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        } finally {
            ThreadJacksonMixInHolder.clear();
        }
        return "";
    }

    public static JsonNode deserialize(String json) {
        try {
            ThreadJacksonMixInHolder mixInHolder = ThreadJacksonMixInHolder.getMixInHolder();
            ObjectMapper objectMapper = mixInHolder.getObjectMapper();
            return objectMapper.readTree(json);
        } catch (IOException e) {
            LOG.error(e.getMessage() + " source json string : " + json + " => readNode", e);
        } finally {
            ThreadJacksonMixInHolder.clear();
        }
        return null;
    }

    public static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER_CACHE.get(DEFAULT_KEY);
    }

    public static <T> T deserialize(String json, Class<T> classed) {
        try {
            ThreadJacksonMixInHolder mixInHolder = ThreadJacksonMixInHolder.getMixInHolder();
            ObjectMapper objectMapper = mixInHolder.getObjectMapper();
            if (!ClassUtil.isArray(classed) && json.startsWith("[") && json.endsWith("]")) {
                json = json.substring(1, json.length() - 2);
            }
            return objectMapper.readValue(json, classed);
        } catch (IOException e) {
            LOG.error(e.getMessage() + " source json string : " + json + " => " + classed, e);
        } finally {
            ThreadJacksonMixInHolder.clear();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] deserialize(String json, T[] classed) {
        try {
            ThreadJacksonMixInHolder mixInHolder = ThreadJacksonMixInHolder.getMixInHolder();
            ObjectMapper objectMapper = mixInHolder.getObjectMapper();
            return (T[]) objectMapper.readValue(json, classed.getClass());
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        } finally {
            ThreadJacksonMixInHolder.clear();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> T deserialize(String json, TypeReference<T> typeReference) {
        try {
            ThreadJacksonMixInHolder mixInHolder = ThreadJacksonMixInHolder.getMixInHolder();
            ObjectMapper objectMapper = mixInHolder.getObjectMapper();
            return (T) objectMapper.readValue(json, typeReference);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        } finally {
            ThreadJacksonMixInHolder.clear();
        }
        return null;
    }

}