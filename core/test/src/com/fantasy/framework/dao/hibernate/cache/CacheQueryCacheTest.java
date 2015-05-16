package com.fantasy.framework.dao.hibernate.cache;

import com.fantasy.attr.storage.bean.Article;
import com.fantasy.attr.storage.service.ArticleService;
import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class CacheQueryCacheTest {

    private final static Log LOG = LogFactory.getLog(CacheQueryCacheTest.class);

    @Autowired
    private ArticleService articleService;

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
        System.out.println("---------------------------");
        article = articleService.get(18L);
        System.out.println("---------------------------");
        article = articleService.get(18L);
        Assert.assertNotNull(article);

        LOG.debug(article.getTitle());

        LOG.debug(article.getSummary());
    }

}
