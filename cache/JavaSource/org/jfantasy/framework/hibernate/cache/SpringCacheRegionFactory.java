package org.jfantasy.framework.hibernate.cache;

import org.jfantasy.framework.spring.SpringContextUtil;
import org.hibernate.cache.CacheException;
import org.hibernate.cfg.Settings;
import org.springframework.cache.CacheManager;

import java.util.Properties;


public class SpringCacheRegionFactory extends AbstractSpringCacheRegionFactory {

    private static final String SPRING_CACHEMANAGER = "hibernate.cache.spring.cache_manager";

    private static final String DEFAULT_SPRING_CACHEMANAGER = "cacheManager";

    @SuppressWarnings("UnusedDeclaration")
    public SpringCacheRegionFactory() {
    }

    @SuppressWarnings("UnusedDeclaration")
    public SpringCacheRegionFactory(Properties prop) {
        super();
    }

    @Override
    public void start(Settings settings, Properties properties) throws CacheException {
        this.settings = settings;
        if (manager == null) {
            manager = SpringContextUtil.getBean(properties.getProperty(SPRING_CACHEMANAGER, DEFAULT_SPRING_CACHEMANAGER), CacheManager.class);
        }
    }

    @Override
    public void stop() {
        if (manager != null) {
            manager = null;
        }
    }

}