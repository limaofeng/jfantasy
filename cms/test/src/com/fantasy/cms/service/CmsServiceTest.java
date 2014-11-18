package com.fantasy.cms.service;


import com.fantasy.attr.bean.Attribute;
import com.fantasy.cms.bean.Article;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.util.common.DateUtil;
import com.fantasy.framework.util.ognl.OgnlUtil;
import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class CmsServiceTest {

    private static Log logger = LogFactory.getLog(CmsServiceTest.class);

    @Resource
    private CmsService cmsService;

    @Test
    @Transient
    public void findPager() {
        Pager<Article> pager = new Pager<Article>();
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        filters.add(new PropertyFilter("EQS_test", "limaofeng1234"));
        Assert.assertNotNull(cmsService.findPager(pager, filters));
    }

    @Test
    @Transient
    public void save() {
        List<Article> articles = this.cmsService.getArticles(new Criterion[]{Restrictions.isNotNull("version")}, 1);
        if (!articles.isEmpty()) {
            Article article = this.cmsService.get(articles.get(0).getId());
            if (!article.getAttributeValues().isEmpty()) {
                Attribute attribute = article.getAttributeValues().get(0).getAttribute();
                OgnlUtil.getInstance().setValue(attribute.getCode(), article, "123456");
                article.setTitle("JUnit测试修改标题-" + DateUtil.format("yyyy-MM-dd"));
                cmsService.save(article);
            }
        }
        articles = this.cmsService.getArticles(new Criterion[]{Restrictions.isNull("version")}, 1);
        if (!articles.isEmpty()) {
            Article article = this.cmsService.get(articles.get(0).getId());
            article.setTitle("JUnit测试修改标题-" + DateUtil.format("yyyy-MM-dd"));
            cmsService.save(article);
        }
    }

    @Test
    public void get() {
        List<Article> articles = this.cmsService.getArticles(new Criterion[]{Restrictions.isNotNull("version")}, 1);
        if (!articles.isEmpty()) {
            Article article = this.cmsService.get(articles.get(0).getId());
            //test not null
            Assert.assertNotNull(article);
            //test attrubuteValues not null
            Assert.assertNotNull(article.getAttributeValues());
        }
        articles = this.cmsService.getArticles(new Criterion[]{Restrictions.isNull("version")}, 1);
        if (!articles.isEmpty()) {
            Article article = this.cmsService.get(articles.get(0).getId());
            //test not null
            Assert.assertNotNull(article);
        }

    }

}
