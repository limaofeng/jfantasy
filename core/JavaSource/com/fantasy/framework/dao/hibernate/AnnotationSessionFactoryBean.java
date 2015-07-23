package com.fantasy.framework.dao.hibernate;

import com.fantasy.attr.storage.listener.AttributeVersionEventListener;
import com.fantasy.attr.storage.listener.VersionChangedEventListener;
import com.fantasy.file.listener.FileManagerEventListener;
import com.fantasy.framework.dao.hibernate.event.PropertyGeneratorSaveOrUpdatEventListener;
import com.fantasy.framework.dao.hibernate.generator.SequenceGenerator;
import com.fantasy.framework.dao.hibernate.generator.SerialNumberGenerator;
import com.fantasy.framework.dao.hibernate.interceptors.BusEntityInterceptor;
import com.fantasy.framework.install.ConfigResolver;
import com.fantasy.framework.lucene.BuguIndex;
import com.fantasy.framework.lucene.dao.hibernate.EntityChangedEventListener;
import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.framework.util.common.ClassUtil;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.framework.util.common.StringUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Interceptor;
import org.hibernate.SessionFactory;
import org.hibernate.cache.spi.RegionFactory;
import org.hibernate.cfg.NamingStrategy;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.util.*;

@SuppressWarnings({"unchecked", "rawtypes"})
public class AnnotationSessionFactoryBean extends LocalSessionFactoryBean implements ResourceLoaderAware {

    private static final Log LOG = LogFactory.getLog(AnnotationSessionFactoryBean.class);

    private Map<String, List<Object>> eventListeners = new HashMap<String, List<Object>>();

    private String[] packagesToScan;

    private Interceptor entityInterceptor;

    private Mode packageMode = Mode.extra;

    private Map<String, Class<? extends IdentifierGenerator>> identifierGenerators = new HashMap<String, Class<? extends IdentifierGenerator>>();

    public static enum Mode {
        extra, basis
    }

    @Override
    public void afterPropertiesSet() throws IOException {
        //========================== 判断是否为扩展模式 ==========================
        if (Mode.extra == packageMode) {
            this.packagesToScan = StringUtil.add(this.packagesToScan == null ? new String[0] : this.packagesToScan, ConfigResolver.parseConfiguration().getPackagesToScan());
            LOG.error(Arrays.toString(packagesToScan));
        }

        //========================== 添加BusEntityInterceptor拦截器 ==========================
        this.entityInterceptor = ObjectUtil.defaultValue(entityInterceptor, new BusEntityInterceptor());

        LocalSessionFactoryBuilder configuration = this.createConfiguration(this.entityInterceptor, this.packagesToScan);
        ClassUtil.setValue(this, "configuration", configuration);

        //========================== 添加自定义ID生成器 ==========================
        identifierGenerators.put("fantasy-sequence", SequenceGenerator.class);
        identifierGenerators.put("serialnumber", SerialNumberGenerator.class);
        for (Map.Entry<String, Class<? extends IdentifierGenerator>> entry : this.identifierGenerators.entrySet()) {
            identifierGenerators.put(entry.getKey(), entry.getValue());
            this.getConfiguration().getIdentifierGeneratorFactory().register(entry.getKey(), entry.getValue());
        }

        SessionFactory sessionFactory = super.buildSessionFactory(configuration);
        ClassUtil.setValue(this, "sessionFactory", sessionFactory);

        //========================== 添加默认监听器 ==========================
        Map<String, List<Object>> eventListenerMap = new HashMap<String, List<Object>>();
        for (Map.Entry<String, List<Object>> entry : this.eventListeners.entrySet()) {
            addEventListener(entry.getKey(), eventListenerMap, entry.getValue());
        }
        //添加动态属性版本监听
        addEventListener(EventType.DELETE.eventName(),eventListenerMap,SpringContextUtil.getBeanByType(AttributeVersionEventListener.class));
        //========================== 添加非主键的序列生成器 ==========================
        addEventListener("save-update", eventListenerMap, SpringContextUtil.getBeanByType(PropertyGeneratorSaveOrUpdatEventListener.class));
        // 添加 lucene 索引生成监听
        if (SpringContextUtil.getBeanByType(BuguIndex.class) != null) {
            EntityChangedEventListener entityChangedEventListener = new EntityChangedEventListener();
            addEventListener("post-insert", eventListenerMap, entityChangedEventListener);
            addEventListener("post-update", eventListenerMap, entityChangedEventListener);
            addEventListener("post-delete", eventListenerMap, entityChangedEventListener);
        }

        VersionChangedEventListener versionChangedEventListener = new VersionChangedEventListener();
        addEventListener("post-insert", eventListenerMap, versionChangedEventListener);
        addEventListener("post-update", eventListenerMap, versionChangedEventListener);
        addEventListener("post-delete", eventListenerMap, versionChangedEventListener);

        // FileEventListener 监听器,用户转存文件或者删除文件
        FileManagerEventListener fileManagerEventListener = SpringContextUtil.createBean(FileManagerEventListener.class, SpringContextUtil.AutoType.AUTOWIRE_BY_TYPE);
		addEventListener("post-insert", eventListenerMap, fileManagerEventListener);
		addEventListener("post-update", eventListenerMap, fileManagerEventListener);
		addEventListener("post-delete", eventListenerMap, fileManagerEventListener);

        this.eventListeners.putAll(eventListenerMap);
        EventListenerRegistry registry = ((SessionFactoryImpl) sessionFactory).getServiceRegistry().getService(EventListenerRegistry.class);
        for (Map.Entry<String, List<Object>> event : this.eventListeners.entrySet()) {
            for (Object listener : event.getValue()) {
                if(EventType.SAVE_UPDATE.eventName().equals(event.getKey())){
                    registry.prependListeners(EventType.resolveEventTypeByName(event.getKey()),listener);
                }else{
                    registry.appendListeners(EventType.resolveEventTypeByName(event.getKey()),listener);
                }
            }
        }

    }

    private LocalSessionFactoryBuilder createConfiguration(Interceptor entityInterceptor, String[] packagesToScan) throws IOException {
        DataSource dataSource = (DataSource) ClassUtil.getValue(this, "dataSource");
        Resource[] configLocations = (Resource[]) ClassUtil.getValue(this, "configLocations");
        String[] mappingResources = (String[]) ClassUtil.getValue(this, "mappingResources");
        Resource[] mappingLocations = (Resource[]) ClassUtil.getValue(this, "mappingLocations");
        Resource[] cacheableMappingLocations = (Resource[]) ClassUtil.getValue(this, "cacheableMappingLocations");
        Resource[] mappingJarLocations = (Resource[]) ClassUtil.getValue(this, "mappingJarLocations");
        Resource[] mappingDirectoryLocations = (Resource[]) ClassUtil.getValue(this, "mappingDirectoryLocations");
        NamingStrategy namingStrategy = (NamingStrategy) ClassUtil.getValue(this, "namingStrategy");
        Object jtaTransactionManager = ClassUtil.getValue(this, "jtaTransactionManager");
        Object multiTenantConnectionProvider = ClassUtil.getValue(this, "multiTenantConnectionProvider");
        Object currentTenantIdentifierResolver = ClassUtil.getValue(this, "currentTenantIdentifierResolver");
        RegionFactory cacheRegionFactory = (RegionFactory) ClassUtil.getValue(this, "cacheRegionFactory");
        TypeFilter[] entityTypeFilters = (TypeFilter[]) ClassUtil.getValue(this, "entityTypeFilters");
        Properties hibernateProperties = (Properties) ClassUtil.getValue(this, "hibernateProperties");
        Class<?>[] annotatedClasses = (Class<?>[]) ClassUtil.getValue(this, "annotatedClasses");
        String[] annotatedPackages = (String[]) ClassUtil.getValue(this, "annotatedPackages");
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        LocalSessionFactoryBuilder sfb = new LocalSessionFactoryBuilder(dataSource, resourcePatternResolver);
        if (configLocations != null) {
            for (Resource resource : configLocations) {
                // Load Hibernate configuration from given location.
                sfb.configure(resource.getURL());
            }
        }
        if (mappingResources != null) {
            // Register given Hibernate mapping definitions, contained in resource files.
            for (String mapping : mappingResources) {
                Resource mr = new ClassPathResource(mapping.trim(), resourcePatternResolver.getClassLoader());
                sfb.addInputStream(mr.getInputStream());
            }
        }
        if (mappingLocations != null) {
            // Register given Hibernate mapping definitions, contained in resource files.
            for (Resource resource : mappingLocations) {
                sfb.addInputStream(resource.getInputStream());
            }
        }
        if (cacheableMappingLocations != null) {
            // Register given cacheable Hibernate mapping definitions, read from the file system.
            for (Resource resource : cacheableMappingLocations) {
                sfb.addCacheableFile(resource.getFile());
            }
        }
        if (mappingJarLocations != null) {
            // Register given Hibernate mapping definitions, contained in jar files.
            for (Resource resource : mappingJarLocations) {
                sfb.addJar(resource.getFile());
            }
        }
        if (mappingDirectoryLocations != null) {
            // Register all Hibernate mapping definitions in the given directories.
            for (Resource resource : mappingDirectoryLocations) {
                File file = resource.getFile();
                if (!file.isDirectory()) {
                    throw new IllegalArgumentException(
                            "Mapping directory location [" + resource + "] does not denote a directory");
                }
                sfb.addDirectory(file);
            }
        }
        if (entityInterceptor != null) {
            sfb.setInterceptor(entityInterceptor);
        }
        if (namingStrategy != null) {
            sfb.setNamingStrategy(namingStrategy);
        }
        if (jtaTransactionManager != null) {
            sfb.setJtaTransactionManager(jtaTransactionManager);
        }
        if (multiTenantConnectionProvider != null) {
            sfb.setMultiTenantConnectionProvider(multiTenantConnectionProvider);
        }
        if (currentTenantIdentifierResolver != null) {
            sfb.setCurrentTenantIdentifierResolver(currentTenantIdentifierResolver);
        }
        if (cacheRegionFactory != null) {
            sfb.setCacheRegionFactory(cacheRegionFactory);
        }
        if (entityTypeFilters != null) {
            sfb.setEntityTypeFilters(entityTypeFilters);
        }
        if (hibernateProperties != null) {
            sfb.addProperties(hibernateProperties);
        }
        if (annotatedClasses != null) {
            sfb.addAnnotatedClasses(annotatedClasses);
        }
        if (annotatedPackages != null) {
            sfb.addPackages(annotatedPackages);
        }
        if (packagesToScan != null) {
            sfb.scanPackages(packagesToScan);
        }
        //getEntityPersisterClass
        return sfb;
    }

    private void addEventListener(String eventName, Map<String, List<Object>> listeners, Object listenerObject) {
        if (!listeners.containsKey(eventName)) {
            listeners.put(eventName, new ArrayList());
        }
        List listListener = listeners.get(eventName);
        if (listenerObject != null) {
            if (listenerObject instanceof Collection<?>) {
                listListener.addAll((Collection) listenerObject);
            } else {
                listListener.add(listenerObject);
            }
        }
    }


    public void setPackagesToScan(String[] packagesToScan) {
        this.packagesToScan = new String[0];
        for (String packagez : packagesToScan) {
            this.packagesToScan = StringUtil.add(this.packagesToScan, StringUtils.tokenizeToStringArray(packagez, ",; \t\n"));
        }
    }

    public void setPackagesToScan(String packagesToScan) {
        this.packagesToScan = StringUtils.tokenizeToStringArray(packagesToScan, ",; \t\n");
    }

    public void setPackageMode(Mode packageMode) {
        this.packageMode = packageMode;
    }

    public void setIdentifierGenerators(Map<String, Class<? extends IdentifierGenerator>> identifierGenerators) {
        this.identifierGenerators = identifierGenerators;
    }

    public void setEventListeners(Map<String, List<Object>> eventListeners) {
        this.eventListeners = eventListeners;
    }

    @Override
    public void setEntityInterceptor(Interceptor entityInterceptor) {
        this.entityInterceptor = entityInterceptor;
    }

}