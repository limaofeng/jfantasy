package com.fantasy.framework.hibernate.cache.nonstop;

import com.fantasy.framework.hibernate.cache.log.SpringCacheMessageLogger;
import org.jboss.logging.Logger;

public final class HibernateNonstopCacheExceptionHandler {
    public static final String HIBERNATE_THROW_EXCEPTION_ON_TIMEOUT_PROPERTY = "springcache.hibernate.propagateNonStopCacheException";
    public static final String HIBERNATE_LOG_EXCEPTION_STACK_TRACE_PROPERTY = "springcache.hibernate.logNonStopCacheExceptionStackTrace";

    private static final SpringCacheMessageLogger LOG = Logger.getMessageLogger(SpringCacheMessageLogger.class, HibernateNonstopCacheExceptionHandler.class.getName());
    private static final HibernateNonstopCacheExceptionHandler INSTANCE = new HibernateNonstopCacheExceptionHandler();

    private HibernateNonstopCacheExceptionHandler() {
    }

    public static HibernateNonstopCacheExceptionHandler getInstance() {
        return INSTANCE;
    }

    public void handleNonstopCacheException(NonStopCacheException nonStopCacheException) {
        if (Boolean.getBoolean(HIBERNATE_THROW_EXCEPTION_ON_TIMEOUT_PROPERTY)) {
            throw nonStopCacheException;
        } else {
            if (Boolean.getBoolean(HIBERNATE_LOG_EXCEPTION_STACK_TRACE_PROPERTY)) {
                LOG.debug("Ignoring NonstopCacheException - " + nonStopCacheException.getMessage(), nonStopCacheException);
            } else {
                LOG.debug("Ignoring NonstopCacheException - " + nonStopCacheException.getMessage());
            }
        }
    }
}