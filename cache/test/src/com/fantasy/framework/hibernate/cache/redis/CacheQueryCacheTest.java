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
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class CacheQueryCacheTest {

    private final static Log LOG = LogFactory.getLog(CacheQueryCacheTest.class);

    @Autowired
    private ArticleService articleService;
    @Autowired
    private RedisCacheManager redisCacheManager;
    @Autowired
    private WebsiteService websiteService;

    private Article article;

//    @Before
    public void setUp() throws Exception {
        article = new Article();
        article.setTitle("测试缓存文章");
        article.setSummary("测试缓存文章");
        articleService.save(article);
    }

    @After
    public void tearDown() throws Exception {
//        for(Article art : articleService.find(Restrictions.eq("title","测试缓存文章"))){
//            this.articleService.delete(art.getId());
//        }
    }

    @Test
    public void testGet(){
        LOG.debug(articleService.get(18L));

        LOG.debug(articleService.find(Restrictions.eq("title", "测试缓存文章")));

        LOG.debug(articleService.find(Restrictions.eq("category.code", "test")));

        Cache cache = redisCacheManager.getCache(Article.class.getName());

        CacheKey cacheKey = new CacheKey(18L, LongType.INSTANCE,Article.class.getName(),null,null);

        Object object = cache.get(cacheKey);

        LOG.debug(object);
    }

    @Test
    public void testGetWebSite(){
        Website website = websiteService.findUniqueByKey("haolue");
        LOG.debug(website);
        website = websiteService.findUniqueByKey("haolue");
        LOG.debug(website);
    }

}
