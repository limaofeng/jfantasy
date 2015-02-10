package com.fantasy.swp;

import com.fantasy.attr.bean.Article;
import com.fantasy.attr.service.ArticleService;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by hebo on 2015/2/6.
 * 监听测试
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class EntityChangedListenerTest {

    @Resource
    private ArticleService articleService;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }


    @Test
    public void testUpdate(){
        List<Article> articleList = this.articleService.find(new ArrayList<PropertyFilter>());
        if(articleList!=null){
            Article article = articleList.get(0);
            article.setTitle("改变title数据"+new Date());
            this.articleService.save(article);
        }

    }
}
