package com.fantasy.framework.hibernate.cache.redis;

import com.fantasy.system.bean.Website;
import com.fantasy.system.service.WebsiteService;
import com.fantasy.test.bean.Article;
import com.fantasy.test.service.ArticleService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.cache.spi.CacheKey;
import org.hibernate.criterion.Restrictions;
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
    private ArticleService articleService;
    @Autowired
    private RedisCacheManager redisCacheManager;
    @Autowired
    private WebsiteService websiteService;

    @Test
    public void testGet() {
        LOG.debug(articleService.get(1801L));

        LOG.debug(articleService.find(Restrictions.eq("title", "测试缓存文章")));

        LOG.debug(articleService.find(Restrictions.eq("category.code", "test")));

        Cache cache = redisCacheManager.getCache(Article.class.getName());

        CacheKey cacheKey = new CacheKey(1801L, LongType.INSTANCE, Article.class.getName(), null, null);

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
