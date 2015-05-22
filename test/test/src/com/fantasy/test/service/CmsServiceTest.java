package com.fantasy.test.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.lucene.BuguParser;
import com.fantasy.test.bean.Article;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class CmsServiceTest {

    private static final Log LOG = LogFactory.getLog(CmsServiceTest.class);

    @Autowired
    private CmsService cmsService;

    @Test
    public void testSearch() throws Exception {
        Pager<Article> pager = new Pager<Article>();
        pager = cmsService.search(pager, BuguParser.parse("title","上海"));
        LOG.debug("size = " + pager.getTotalCount());
        for(Article article : pager.getPageItems()){
            LOG.debug("title = " + article.getTitle());
        }
    }

}