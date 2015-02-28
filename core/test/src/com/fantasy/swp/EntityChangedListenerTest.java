package com.fantasy.swp;

import com.fantasy.attr.bean.Article;
import com.fantasy.attr.service.ArticleService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by hebo on 2015/2/6.
 * 监听测试
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class EntityChangedListenerTest {

    @Resource
    private ArticleService articleService;

    private Article article;

    @Before
    public void setUp() throws Exception {
        this.article = new Article();
        this.article.setTitle("测试标题");
        articleService.save(article);
    }

    @After
    public void tearDown() throws Exception {
        articleService.delete(article.getId());
    }

    @Test
    public void testUpdate() {
        Article article = new Article();
        article.setId(this.article.getId());
        article.setTitle("改变title数据" + new Date());
        this.articleService.save(article);
    }
}
