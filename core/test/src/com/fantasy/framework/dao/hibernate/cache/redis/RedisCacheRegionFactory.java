package com.fantasy.framework.dao.hibernate.cache.redis;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.cache.CacheException;
import org.hibernate.cfg.Settings;

import java.util.Properties;


/**
 * A non-singleton EhCacheRegionFactory implementation.
 *
 * @author Chris Dennis
 * @author Greg Luck
 * @author Emmanuel Bernard
 * @author Abhishek Sanoujam
 * @author Alex Snaps
 */
public class RedisCacheRegionFactory extends AbstractRedisRegionFactory {

    private static final Log LOG = LogFactory.getLog(RedisCacheRegionFactory.class);

    @SuppressWarnings("UnusedDeclaration")
    public RedisCacheRegionFactory() {
    }

    @SuppressWarnings("UnusedDeclaration")
    public RedisCacheRegionFactory(Properties prop) {
        super();
    }

    @Override
    public void start(Settings settings, Properties properties) throws CacheException {
        this.settings = settings;
        if (manager != null) {
            LOG.debug("已经初始化!");
        }
    }

    @Override
    public void stop() {
        try {
            if (manager != null) {
                mbeanRegistrationHelper.unregisterMBean();
                manager = null;
            }
        } catch (net.sf.ehcache.CacheException e) {
            throw new CacheException(e);
        }
    }

}