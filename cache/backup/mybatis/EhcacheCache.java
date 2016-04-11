package org.jfantasy.framework.dao.mybatis.cache;

import org.jfantasy.framework.util.common.ObjectUtil;
import org.apache.ibatis.cache.Cache;
import org.springframework.beans.factory.InitializingBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 扩展mybatis缓存，支持Ehcache
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2012-10-28 下午08:28:47
 */
public final class EhcacheCache extends DefaultEhCache implements Cache, InitializingBean {
    private static List<EhcacheCache> lazys = new ArrayList<EhcacheCache>();

    public void afterPropertiesSet() throws Exception {
        for (EhcacheCache ehCache : lazys) {
            if ((ObjectUtil.isNotNull(this.cacheManager)) && (!this.cacheManager.cacheExists(ehCache.getId()))) {
                Ehcache cache = this.cacheManager.getCache(ehCache.getId());
                if (ObjectUtil.isNull(cache)) {
                    this.cacheManager.addCache(ehCache.getId());
                }
                ehCache.setCacheManager(this.cacheManager);
            }
        }

    }

    public EhcacheCache(String id) {
        super(id);
        if ((ObjectUtil.isNull(this.cacheManager)) || (!this.cacheManager.cacheExists(id))) {
            lazys.add(this);
        }
    }

    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }
}