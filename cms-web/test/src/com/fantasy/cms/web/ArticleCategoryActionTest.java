package com.fantasy.cms.web;

import com.fantasy.cms.bean.ArticleCategory;
import com.fantasy.cms.service.CmsService;
import com.fantasy.framework.struts2.StrutsSpringJUnit4TestCase;
import com.fantasy.security.SpringSecurityUtils;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionProxy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class ArticleCategoryActionTest extends StrutsSpringJUnit4TestCase {

    private final static Log LOG = LogFactory.getLog(ArticleCategoryActionTest.class);

    @Override
    protected String getConfigPath() {
        return "struts.xml";
    }

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private CmsService cmsService;

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
    }

    @Test
    public void testView() throws Exception {
        this.request.setMethod("GET");

        ActionProxy proxy = super.getActionProxy("/cms/categorys/test");
        Assert.assertNotNull(proxy);

        String result = proxy.execute();

        LOG.debug("content:" + this.response.getContentAsString());

        Assert.assertEquals(Action.SUCCESS, result);
    }

    @Test
    public void testCreate() throws Exception {
        this.request.setMethod("POST");

        this.request.addParameter("code", "testsave");
        this.request.addParameter("name", "JUnit测试");
        this.request.addParameter("description", "test");

        ActionProxy proxy = super.getActionProxy("/cms/categorys");
        Assert.assertNotNull(proxy);

        String result = proxy.execute();

        LOG.debug("content:" + this.response.getContentAsString());

        Assert.assertEquals(Action.SUCCESS, result);
    }

    @Test
    public void testSearch() throws Exception {
        this.request.setMethod("GET");
        this.request.addParameter("EQS_code", "testsave");
        ActionProxy proxy = super.getActionProxy("/cms/categorys");
        Assert.assertNotNull(proxy);
        String result = proxy.execute();
        LOG.debug("content:" + this.response.getContentAsString());
        Assert.assertEquals(Action.SUCCESS, result);
    }

    @Test
    public void testUpdate() throws Exception {
        this.request.setMethod("PUT");
        this.request.addParameter("code", "testsave");
        this.request.addParameter("name", "JUnit测试");
        this.request.addParameter("description", "test");
        ActionProxy proxy = super.getActionProxy("/cms/categorys/testsave");
        Assert.assertNotNull(proxy);
        String result = proxy.execute();
        LOG.debug("content:" + this.response.getContentAsString());
        Assert.assertEquals(Action.SUCCESS, result);
    }

    public void testBatchDelete() throws Exception {
        this.request.setMethod("DELETE");
        this.request.addParameter("id","test1");
        this.request.addParameter("id","test2");
        ActionProxy proxy = super.getActionProxy("/cms/categorys");
        Assert.assertNotNull(proxy);
        String result = proxy.execute();
        LOG.debug("content:" + this.response.getContentAsString());
        Assert.assertEquals(Action.SUCCESS, result);
    }

    @Test
    public void testDelete() throws Exception {
        this.request.setMethod("DELETE");
        ActionProxy proxy = super.getActionProxy("/cms/categorys/test");
        Assert.assertNotNull(proxy);
        String result = proxy.execute();
        LOG.debug("content:" + this.response.getContentAsString());
        Assert.assertEquals(Action.SUCCESS, result);
    }

}