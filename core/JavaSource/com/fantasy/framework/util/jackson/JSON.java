package com.fantasy.framework.util.jackson;

import com.fantasy.framework.spring.mvc.http.jsonfilter.NoneFieldsBeanPropertyFilter;
import com.fantasy.framework.util.common.ClassUtil;
import com.fantasy.framework.util.jackson.deserializer.DateDeserializer;
import com.fantasy.framework.util.jackson.serializer.DateSerializer;
import com.fantasy.framework.util.jackson.serializer.StringUnicodeSerializer;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class JSON {

    private static final Log LOG = LogFactory.getLog(JSON.class);

    public static final String DEFAULT_KEY = "default";
    public static final String UNICODE_KEY = "unicode";
    private static final ConcurrentHashMap<String, ObjectMapper> OBJECT_MAPPER_CACHE = new ConcurrentHashMap<String, ObjectMapper>();

    private static final ConcurrentHashMap<Class, String[]> IGNORE_PROPERTIES_CACHE = new ConcurrentHashMap<Class, String[]>();

    private static final Mirror MIRROR = new Mirror();

    public static final String CUSTOM_FILTER = "customFilter";

    public static class Mirror {

        public String serialize(Object object, String... ignoreProperties) {
            return JSON.serialize(object, ignoreProperties);
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

    public static class ThreadLocalObjectMapper {

        private ObjectMapper objectMapper;

        private String[] ignoreProperties;

        public ThreadLocalObjectMapper(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
        }

        public ObjectMapper getObjectMapper() {
            return objectMapper;
        }

        public String[] getIgnoreProperties() {
            return ignoreProperties;
        }

        public void setIgnoreProperties(String[] ignoreProperties) {
            this.ignoreProperties = ignoreProperties;
        }
    }

    private static ThreadLocal<ThreadLocalObjectMapper> threadLocal = new ThreadLocal<ThreadLocalObjectMapper>();

    static {
        //默认
        register(DEFAULT_KEY, new ObjectMapperRegister() {
            @Override
            public void callback(ObjectMapper objectMapper) {
                objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
                // 当找不到对应的序列化器时 忽略此字段
                objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
                // 允许非空字段
                objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
                // 允许单引号
                objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
                // 设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                //使Jackson JSON支持Unicode编码非ASCII字符
                SimpleModule module = new SimpleModule();
                module.addSerializer(Date.class, new DateSerializer("yyyy-MM-dd HH:mm:ss"));
                module.addDeserializer(Date.class, new DateDeserializer());
                objectMapper.registerModule(module);

                objectMapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector() {

                    @Override
                    public String[] findPropertiesToIgnore(Annotated ac) {
                        JsonFilter jsonFilter = ac.getAnnotation(JsonFilter.class);
                        if (jsonFilter != null && CUSTOM_FILTER.equals(jsonFilter.value())) {
                            IGNORE_PROPERTIES_CACHE.put(ac.getRawType(), super.findPropertiesToIgnore(ac));
                            return new String[0];
                        }
                        return super.findPropertiesToIgnore(ac);
                    }
                });

                objectMapper.setFilters(new SimpleFilterProvider().addFilter(JSON.CUSTOM_FILTER, new NoneFieldsBeanPropertyFilter()));
            }
        });
        //将中文转为 Unicode 编码
        register(UNICODE_KEY, new ObjectMapperRegister() {
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
                module.addDeserializer(Date.class, new DateDeserializer());
                objectMapper.registerModule(module);
            }
        });
    }

    /**
     * 返回 Key 对应的镜像
     *
     * @return Mirror
     */
    public static Mirror set(String key) {
        threadLocal.set(new ThreadLocalObjectMapper(OBJECT_MAPPER_CACHE.get(key)));
        return MIRROR;
    }

    public static Mirror unicode() {
        threadLocal.set(new ThreadLocalObjectMapper(OBJECT_MAPPER_CACHE.get(UNICODE_KEY)));
        return MIRROR;
    }

    public synchronized static void register(String key, ObjectMapperRegister instanceCallBack) {
        ObjectMapper objectMapper = new ObjectMapper();
        instanceCallBack.callback(objectMapper);
        OBJECT_MAPPER_CACHE.putIfAbsent(key, objectMapper);
    }

    public interface ObjectMapperRegister {

        void callback(ObjectMapper objectMapper);

    }

    public static String serialize(Object object, String... ignoreProperties) {
        try {
            if (object == null) {
                return null;
            }
            ThreadLocalObjectMapper local = threadLocal.get();
            if (local == null) {
                threadLocal.set(local = new ThreadLocalObjectMapper(OBJECT_MAPPER_CACHE.get(DEFAULT_KEY)));
            }
            try {
                ObjectMapper objectMapper = local.getObjectMapper();
                if (ignoreProperties.length == 0) {
                    return objectMapper.writeValueAsString(object);
                }
                JsonFilter jsonFilter = ClassUtil.getClassGenricType(object.getClass(), JsonFilter.class);
                if (jsonFilter == null) {
                    LOG.error("未设置 JsonFilter 不能使用 ignoreProperties 参数进行字段过滤");
                    return null;
                }
                local.setIgnoreProperties(ignoreProperties);
                return objectMapper.writeValueAsString(object);
            } finally {
                threadLocal.remove();
            }
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
        return "";
    }

    public static Object deserialize(String json) {
        return deserialize(json, HashMap.class);
    }

    public static ObjectMapper getObjectMapper() {
        ThreadLocalObjectMapper local = threadLocal.get();
        if (local == null) {
            threadLocal.set(local = new ThreadLocalObjectMapper(OBJECT_MAPPER_CACHE.get(DEFAULT_KEY)));
        }
        try {
            return local.getObjectMapper();
        } finally {
            threadLocal.remove();
        }
    }

    public static <T> T deserialize(String json, Class<T> classed) {
        try {
            ThreadLocalObjectMapper local = threadLocal.get();
            if (local == null) {
                threadLocal.set(local = new ThreadLocalObjectMapper(OBJECT_MAPPER_CACHE.get(DEFAULT_KEY)));
            }
            try {
                if (!ClassUtil.isArray(classed) && json.startsWith("[") && json.endsWith("]")) {
                    json = json.substring(1, json.length() - 2);
                }
                return local.getObjectMapper().readValue(json, classed);
            } finally {
                threadLocal.remove();
            }
        } catch (IOException e) {
            LOG.error(e.getMessage() + " source json string : " + json + " => " + classed, e);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] deserialize(String json, T[] classed) {
        try {
            ThreadLocalObjectMapper local = threadLocal.get();
            if (local == null) {
                threadLocal.set(local = new ThreadLocalObjectMapper(OBJECT_MAPPER_CACHE.get(DEFAULT_KEY)));
            }
            try {
                return (T[]) local.getObjectMapper().readValue(json, classed.getClass());
            } finally {
                threadLocal.remove();
            }
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
        return null;
    }


    @SuppressWarnings("unchecked")
    public static <T> T deserialize(String json, TypeReference<T> typeReference) {
        try {
            ThreadLocalObjectMapper local = threadLocal.get();
            if (local == null) {
                threadLocal.set(local = new ThreadLocalObjectMapper(OBJECT_MAPPER_CACHE.get(DEFAULT_KEY)));
            }
            try {
                return (T) local.getObjectMapper().readValue(json, typeReference);
            } finally {
                threadLocal.remove();
            }
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
        return null;
    }

    public static String[] getIgnoreProperties(Class clazz) {
        if(!IGNORE_PROPERTIES_CACHE.containsKey(clazz)){
            JSON.serialize(ClassUtil.newInstance(clazz));
            if(!IGNORE_PROPERTIES_CACHE.containsKey(clazz)){
                IGNORE_PROPERTIES_CACHE.put(clazz,new String[0]);
            }
        }
        return IGNORE_PROPERTIES_CACHE.get(clazz);
    }

}