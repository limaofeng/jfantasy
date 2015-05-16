package com.fantasy.framework.dao.hibernate.cache.redis;


import com.fantasy.attr.storage.bean.Article;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class RedisTest {

    @Autowired
    private RedisCacheManager redisCacheManager;

    @Test
    public void testRedisCacheManager(){
        Assert.assertNotNull(redisCacheManager);

        Cache cache = redisCacheManager.getCache("test");

        Article article = cache.get("article:1",Article.class);

        Assert.assertNotNull(article);

        article = new Article();
        article.setTitle("测试");
        article.setSummary("1235");
        cache.put("article:1",article);

        article = cache.get("article:1",Article.class);

        Assert.assertEquals(article.getTitle(),"测试");
        Assert.assertEquals(article.getSummary(),"1235");
    }

}
