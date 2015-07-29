package com.fantasy.framework.dao.mybatis.cache;

import org.apache.ibatis.cache.decorators.LoggingCache;

/**
 * 扩展mybatis缓存，支持Ehcache
 *
 * @author 李茂峰
 * @version 1.0
 * @功能描述
 * @since 2012-10-28 下午08:29:52
 */
public class LoggingEhcache extends LoggingCache {

    public LoggingEhcache(String id) {
        super(new EhcacheCache(id));
    }

}