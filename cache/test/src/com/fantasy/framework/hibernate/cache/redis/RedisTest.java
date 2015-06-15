package com.fantasy.framework.hibernate.cache.redis;


import com.fantasy.test.bean.Article;
import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
public class RedisTest {

    private final static Log LOG = LogFactory.getLog(RedisTest.class);

    @Autowired
    private RedisCacheManager redisCacheManager;

    @Test
    public void testRedisCacheManager(){
        Assert.assertNotNull(redisCacheManager);

        Cache cache = redisCacheManager.getCache("test");

        cache.evict("article:1");

        Article article = cache.get("article:1",Article.class);

        LOG.debug(article);

        article = new Article();
        article.setTitle("测试");
        article.setSummary("1235");
        cache.put("article:1",article);

        article = cache.get("article:1",Article.class);

        Assert.assertEquals(article.getTitle(),"测试");
        Assert.assertEquals(article.getSummary(),"1235");
    }

}
