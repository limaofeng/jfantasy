package com.fantasy.cms.web;

import com.fantasy.cms.bean.Article;
import com.fantasy.cms.bean.ArticleCategory;
import com.fantasy.cms.service.CmsService;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.struts2.core.context.ActionConstants;
import com.fantasy.framework.util.common.DateUtil;
import com.fantasy.security.SpringSecurityUtils;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionProxy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.fantasy.framework.struts2.StrutsSpringJUnit4TestCase;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.ArrayList;
import java.util.List;

/**
 * action 测试
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class CmsActionTest extends StrutsSpringJUnit4TestCase {

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private CmsService cmsService;

    private final static Log LOG = LogFactory.getLog(CmsActionTest.class);

    //加载struts文件
    @Override
    protected String getConfigPath() {
        return "struts.xml";
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        //默认登陆用户
        UserDetails userDetails = userDetailsService.loadUserByUsername("admin");
        SpringSecurityUtils.saveUserDetailsToContext(userDetails, request);
        request.getSession().setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
        //添加文章测试分类
        ArticleCategory category = new ArticleCategory();
        category.setCode("test");
        category.setName("JUnit测试");
        category.setDescription("test");
        cmsService.save(category);
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
        //删除测试分类
        ArticleCategory category = cmsService.get("test");
        cmsService.remove(category.getCode());
    }

    @Test
    public void testArticle() throws Exception {
        ActionProxy proxy = super.getActionProxy("/cms/article.do");
        Assert.assertNotNull(proxy);
        proxy.setExecuteResult(false);

        String result = proxy.execute();
        Assert.assertEquals(Action.SUCCESS, result);
    }

    @Test
    public void testIndex() throws Exception {
        //添加查询条件
        this.request.addParameter("EQS_title","test");

        ActionProxy proxy = super.getActionProxy("/cms/article/index.do");
        Assert.assertNotNull(proxy);
        proxy.setExecuteResult(false);

        String result = proxy.execute();
        Assert.assertEquals(Action.SUCCESS, result);
    }

    @Test
    public void testSearch() throws Exception {
        //添加查询条件
        this.request.addParameter("EQS_title","test");

        ActionProxy proxy = super.getActionProxy("/cms/article/search.do");
        Assert.assertNotNull(proxy);

        String result = proxy.execute();
        Assert.assertEquals(ActionConstants.JSONDATA, result);
    }

    @Test
    public void testArticleSave() throws Exception {
        this.request.addParameter("title","测试文章标题");
        this.request.addParameter("summary","测试文章摘要");
        this.request.addParameter("keywords","test");
        this.request.addParameter("content","测试文章正文");
        this.request.addParameter("author","limaofeng");
        this.request.addParameter("releaseDate", DateUtil.format("yyyy-MM-dd"));
        this.request.addParameter("category.code","test");
        this.request.addParameter("issue","true");

        ActionProxy proxy = super.getActionProxy("/cms/article/article_save.do");
        Assert.assertNotNull(proxy);

        String result = proxy.execute();
        Assert.assertEquals(ActionConstants.JSONDATA, result);
    }

    @Test
    public void testArticleEdit() throws Exception {
        this.testArticleSave();
        this.request.removeAllParameters();

        List<Article> articles = this.cmsService.find(new ArrayList<PropertyFilter>() {
            {
                this.add(new PropertyFilter("EQS_keywords", "test"));
            }
        },"id","asc",1);

        this.request.addParameter("id",articles.get(0).getId().toString());

        ActionProxy proxy = super.getActionProxy("/cms/article/article_edit.do");
        Assert.assertNotNull(proxy);
        proxy.setExecuteResult(false);

        String result = proxy.execute();
        Assert.assertEquals(Action.SUCCESS, result);
    }

    @Test
    public void testArticleView() throws Exception {
        this.testArticleSave();
        this.request.removeAllParameters();

        List<Article> articles = this.cmsService.find(new ArrayList<PropertyFilter>() {
            {
                this.add(new PropertyFilter("EQS_keywords", "test"));
            }
        },"id","asc",1);

        this.request.addParameter("id",articles.get(0).getId().toString());

        ActionProxy proxy = super.getActionProxy("/cms/article/article_view.do");
        Assert.assertNotNull(proxy);
        proxy.setExecuteResult(false);

        String result = proxy.execute();
        Assert.assertEquals(Action.SUCCESS, result);
    }

    @Test
    public void testArticleIssue() throws Exception {
        this.testArticleSave();
        this.request.removeAllParameters();

        List<Article> articles = this.cmsService.find(new ArrayList<PropertyFilter>() {
            {
                this.add(new PropertyFilter("EQS_keywords", "test"));
            }
        },"id","asc",1);

        this.request.addParameter("ids",articles.get(0).getId().toString());

        ActionProxy proxy = super.getActionProxy("/cms/article/article_issue.do");
        Assert.assertNotNull(proxy);

        String result = proxy.execute();

        Assert.assertEquals(ActionConstants.JSONDATA, result);
        Article article = this.cmsService.get(articles.get(0).getId());
        Assert.assertTrue(article.getIssue());
    }

    @Test
    public void testColseissue() throws Exception {
        this.testArticleSave();
        this.request.removeAllParameters();

        List<Article> articles = this.cmsService.find(new ArrayList<PropertyFilter>() {
            {
                this.add(new PropertyFilter("EQS_keywords", "test"));
            }
        },"id","asc",1);

        this.request.addParameter("ids",articles.get(0).getId().toString());

        ActionProxy proxy = super.getActionProxy("/cms/article/article_colse.do");
        Assert.assertNotNull(proxy);

        String result = proxy.execute();

        Assert.assertEquals(ActionConstants.JSONDATA, result);
        Article article = this.cmsService.get(articles.get(0).getId());
        Assert.assertFalse(article.getIssue());
    }

    @Test
    public void testArticleDelete() throws Exception {
        this.testArticleSave();
        this.request.removeAllParameters();

        List<Article> articles = this.cmsService.find(new ArrayList<PropertyFilter>() {
            {
                this.add(new PropertyFilter("EQS_keywords", "test"));
            }
        },"id","asc",1);

        this.request.addParameter("ids",articles.get(0).getId().toString());
        ActionProxy proxy = super.getActionProxy("/cms/article/article_delete.do");
        Assert.assertNotNull(proxy);

        String result = proxy.execute();

        Assert.assertEquals(ActionConstants.JSONDATA, result);
        Assert.assertNull(this.cmsService.get(articles.get(0).getId()));

    }

    @Test
    public void testCategorySave() throws Exception {
        this.request.addParameter("code", "testsave");
        this.request.addParameter("name", "JUnit测试");
        this.request.addParameter("description", "test");

        ActionProxy proxy = super.getActionProxy("/cms/article/category_save.do");
        Assert.assertNotNull(proxy);
        proxy.setExecuteResult(false);

        String result = proxy.execute();

        Assert.assertEquals(ActionConstants.JSONDATA, result);
        Assert.assertNotNull(this.cmsService.get("testsave"));
    }

    @Test
    public void testCategoryEdit() throws Exception {
        this.request.addParameter("id", "test");

        ActionProxy proxy = super.getActionProxy("/cms/article/category_edit.do");
        Assert.assertNotNull(proxy);
        proxy.setExecuteResult(false);

        String result = proxy.execute();

        Assert.assertEquals(Action.SUCCESS, result);
    }

    @Test
    public void testCategoryDelete() throws Exception {
        this.testCategorySave();
        this.request.removeAllParameters();

        this.request.addParameter("ids", "testsave");

        ActionProxy proxy = super.getActionProxy("/cms/article/category_delete.do");
        Assert.assertNotNull(proxy);
        proxy.setExecuteResult(false);

        String result = proxy.execute();

        Assert.assertEquals(ActionConstants.JSONDATA, result);
        Assert.assertNull(this.cmsService.get("testsave"));
    }

    @Test
    public void testCategoryAdd() throws Exception {
        this.request.addParameter("categoryCode", "test");

        ActionProxy proxy = super.getActionProxy("/cms/article/category_add.do");
        Assert.assertNotNull(proxy);
        proxy.setExecuteResult(false);

        String result = proxy.execute();

        Assert.assertEquals(Action.SUCCESS, result);
    }

    @Test
    public void testArticleAdd() throws Exception {
        this.request.addParameter("categoryCode", "test");

        ActionProxy proxy = super.getActionProxy("/cms/article/article_add.do");
        Assert.assertNotNull(proxy);
        proxy.setExecuteResult(false);

        String result = proxy.execute();

        Assert.assertEquals(Action.SUCCESS, result);
    }

    @Test
    public void testMoveArticle() throws Exception {
        this.testArticleSave();
        this.request.removeAllParameters();

        List<Article> articles = this.cmsService.find(new ArrayList<PropertyFilter>() {
            {
                this.add(new PropertyFilter("EQS_keywords", "test"));
            }
        },"id","asc",1);

        ArticleCategory category = new ArticleCategory();
        category.setCode("testmove");
        category.setName("JUnit测试");
        category.setDescription("test");
        cmsService.save(category);

        this.request.addParameter("ids", articles.get(0).getId().toString());
        this.request.addParameter("categoryCode", "testmove");

        ActionProxy proxy = super.getActionProxy("/cms/article/move.do");
        Assert.assertNotNull(proxy);

        String result = proxy.execute();

        Assert.assertEquals(ActionConstants.JSONDATA, result);
        Assert.assertEquals("testmove",this.cmsService.get(articles.get(0).getId()).getCategory().getCode());

        category = cmsService.get("testmove");
        cmsService.remove(category.getCode());
    }

}