package com.fantasy.test.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.lucene.BuguParser;
import com.fantasy.test.bean.Article;
import com.fantasy.test.bean.ArticleCategory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class CmsServiceTest {

    private static final Log LOG = LogFactory.getLog(CmsServiceTest.class);

    @Autowired
    private CmsService cmsService;

    @Before
    public void setUp() throws Exception{
        if(cmsService.get("test") == null) {
            ArticleCategory category = new ArticleCategory();
            category.setCode("test");
            category.setName("用于测试的文章分类");
            cmsService.save(category);
        }
    }

    @Test
    public void testSearch() throws Exception {
        Pager<Article> pager = new Pager<Article>();
        pager = cmsService.search(pager, BuguParser.parse("title","设计"));
        LOG.debug("Lucene 查询出来的结果 size = " + pager.getTotalCount());
        for(Article article : pager.getPageItems()){
            LOG.debug("title = " + article.getTitle());
        }
        pager = cmsService.findPager(pager, new ArrayList<PropertyFilter>(){
            {
                this.add(new PropertyFilter("LIKES_title","设计"));
            }
        });
        LOG.debug("数据库 LIKE 查询出来的结果 size = " + pager.getTotalCount());
        for(Article article : pager.getPageItems()){
            LOG.debug("title = " + article.getTitle());
        }
    }

}