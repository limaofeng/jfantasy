package org.jfantasy.framework.jackson;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfantasy.framework.spring.ClassPathScanner;
import org.jfantasy.framework.util.asm.AnnotationDescriptor;
import org.jfantasy.framework.util.asm.AsmUtil;
import org.jfantasy.framework.util.common.StringUtil;

import java.util.*;

/**
 * 在当前线程内保存ObjectMapper供Jackson2HttpMessageConverter使用
 */
public class ThreadJacksonMixInHolder {

    private static final Log LOG = LogFactory.getLog(ThreadJacksonMixInHolder.class);

    private static ThreadLocal<ThreadJacksonMixInHolder> holderThreadLocal = new ThreadLocal<ThreadJacksonMixInHolder>();
    private final static Map<Class<?>, MixInSource> mixInSourceMap = new HashMap<Class<?>, MixInSource>();

    private ObjectMapper objectMapper;
    private Map<String, Set<String>> ignorePropertyNames = new HashMap<String, Set<String>>();
    private Map<String, Set<String>> allowPropertyNames = new HashMap<>();

    private ThreadJacksonMixInHolder(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public static ThreadJacksonMixInHolder getMixInHolder() {
        return getMixInHolder(JSON.getObjectMapper());
    }

    public static ThreadJacksonMixInHolder getMixInHolder(ObjectMapper objectMapper) {
        ThreadJacksonMixInHolder localMixInHolder = holderThreadLocal.get();
        if (localMixInHolder == null) {
            holderThreadLocal.set(new ThreadJacksonMixInHolder(objectMapper));
        }
        return holderThreadLocal.get();
    }

    /**
     * 清除当前线程内的数据
     */
    public static void clear() {
        holderThreadLocal.set(null);
    }

    /**
     * 移除屏蔽限制
     *
     * @param target 对象
     * @param names  字段
     */
    public void removeIgnorePropertyNames(Class<?> target, String... names) {
    }

    public void addIgnorePropertyNames(Class<?> target, String... names) {
        MixInSource mixInSource = createMixInSource(target);
        if (objectMapper.findMixInClassFor(target) == null) {
            if (objectMapper.mixInCount() != 0) {
                this.objectMapper = this.objectMapper.copy();
                LOG.warn("请在初始化JSON时将 target = [" + target.getName() + "] 的 MixIn 添加到 objectMapper 中.");
            }
            this.objectMapper.addMixIn(mixInSource.getTarget(), mixInSource.getMixIn());
        }
        if (!this.ignorePropertyNames.containsKey(mixInSource.getFilterName())) {
            this.ignorePropertyNames.put(mixInSource.getFilterName(), new HashSet<String>(Arrays.asList(names)));
        } else {
            this.ignorePropertyNames.get(mixInSource.getFilterName()).addAll(Arrays.asList(names));
        }
    }

    public void addAllowPropertyNames(Class<?> target, String... names) {
        MixInSource mixInSource = createMixInSource(target);
        if (objectMapper.findMixInClassFor(target) == null) {
            objectMapper.addMixIn(mixInSource.getTarget(), mixInSource.getMixIn());
        }
        if (!this.allowPropertyNames.containsKey(mixInSource.getFilterName())) {
            this.allowPropertyNames.put(mixInSource.getFilterName(), new HashSet<>(Arrays.asList(names)));
        } else {
            this.allowPropertyNames.get(mixInSource.getFilterName()).addAll(Arrays.asList(names));
        }
    }

    public FilterProvider getFilterProvider() {
        SimpleFilterProvider provider = new SimpleFilterProvider().setFailOnUnknownId(false);
        for (Map.Entry<String, Set<String>> entry : this.allowPropertyNames.entrySet()) {
            provider.addFilter(entry.getKey(), SimpleBeanPropertyFilter.filterOutAllExcept(entry.getValue()));
        }
        for (Map.Entry<String, Set<String>> entry : this.ignorePropertyNames.entrySet()) {
            if(!this.allowPropertyNames.containsKey(entry.getKey())) {
                provider.addFilter(entry.getKey(), SimpleBeanPropertyFilter.serializeAllExcept(entry.getValue()));
            }
        }
        return provider;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public ObjectWriter getObjectWriter() {
        return objectMapper.writer(getFilterProvider());
    }

    /**
     * 判断当前线程是否存在MixIn集合
     *
     * @return boolean
     */
    public static boolean isContainsMixIn() {
        return holderThreadLocal.get() != null && (!holderThreadLocal.get().ignorePropertyNames.isEmpty() || !holderThreadLocal.get().allowPropertyNames.isEmpty());
    }

    public boolean isReturnProperty(Class<?> target, String name) {
        return isAllowProperty(target,name) || !isIgnoreProperty(target,name);
    }

    /**
     * 判断属性是否被忽略
     *
     * @param target class
     * @param name   property
     * @return boolean
     */
    public boolean isIgnoreProperty(Class<?> target, String name) {
        MixInSource mixInSource = createMixInSource(target);
        return this.ignorePropertyNames.containsKey(mixInSource.getFilterName()) && this.ignorePropertyNames.get(mixInSource.getFilterName()).contains(name);
    }

    public boolean isAllowProperty(Class<?> target, String name) {
        MixInSource mixInSource = createMixInSource(target);
        return this.allowPropertyNames.containsKey(mixInSource.getFilterName()) && this.allowPropertyNames.get(mixInSource.getFilterName()).contains(name);
    }

    private static MixInSource createMixInSource(Class<?> target) {
        if (!mixInSourceMap.containsKey(target)) {
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            Class mixIn = AsmUtil.makeInterface("org.jfantasy.framework.jackson.mixin." + target.getSimpleName() + "_" + uuid, AnnotationDescriptor.builder(JsonFilter.class).setValue("value", uuid).build(), FilterMixIn.class);
            MixInSource mixInSource = new MixInSource(uuid, target, mixIn);
            mixInSourceMap.put(target, mixInSource);
            return mixInSource;
        }
        return mixInSourceMap.get(target);
    }

    public static Map<Class<?>, Class<?>> getSourceMixins() {
        Map<Class<?>, Class<?>> sourceMixins = new HashMap<Class<?>, Class<?>>();
        for (MixInSource mixInSource : mixInSourceMap.values()) {
            sourceMixins.put(mixInSource.getTarget(), mixInSource.getMixIn());
        }
        return sourceMixins;
    }

    public static void scan(Class<?>... classes) {
        for (Class clazz : classes) {
            createMixInSource(clazz);
        }
    }

    public static void scan(String... basePackages) {
        for (String basePackage : basePackages) {
            if (StringUtil.isBlank(basePackage)) {
                continue;
            }
            scan(ClassPathScanner.getInstance().findAnnotationedClasses(basePackage, JsonIgnoreProperties.class).toArray(new Class[0]));
        }
    }

    public boolean isAllow(Class<?> target) {
        MixInSource mixInSource = createMixInSource(target);
        return this.allowPropertyNames.containsKey(mixInSource.getFilterName());
    }

    private static class MixInSource {
        private String filterName;
        private Class<?> target;
        private Class<?> mixIn;

        public MixInSource(String filterName, Class<?> target, Class<?> mixIn) {
            this.filterName = filterName;
            this.target = target;
            this.mixIn = mixIn;
        }

        public String getFilterName() {
            return filterName;
        }

        public Class<?> getTarget() {
            return target;
        }

        public Class<?> getMixIn() {
            return mixIn;
        }
    }

}