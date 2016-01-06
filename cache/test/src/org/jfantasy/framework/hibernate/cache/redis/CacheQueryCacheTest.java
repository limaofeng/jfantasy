package org.jfantasy.framework.hibernate.cache.redis;

import org.jfantasy.security.bean.User;
import org.jfantasy.system.bean.Website;
import org.jfantasy.system.service.WebsiteService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.cache.spi.CacheKey;
import org.hibernate.type.LongType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class CacheQueryCacheTest {

    private final static Log LOG = LogFactory.getLog(CacheQueryCacheTest.class);

    @Autowired
    private RedisCacheManager redisCacheManager;
    @Autowired
    private WebsiteService websiteService;

//    @Test
    public void testGet() {
        User user = new User();
        user.setId(0l);
        user.setNickName("张国荣");
        LOG.debug(user);

        LOG.debug(user);

        Cache cache = redisCacheManager.getCache(User.class.getName());

        CacheKey cacheKey = new CacheKey(1801L, LongType.INSTANCE, User.class.getName(), null, null);

        Object object = cache.get(cacheKey);

        LOG.debug(object);
    }

    @Test
    public void testGetWebSite() {
        Website website = websiteService.findUniqueByKey("haolue");
        LOG.debug(website);
        website = websiteService.findUniqueByKey("haolue");
        LOG.debug(website);
    }

}
