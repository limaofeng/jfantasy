package com.fantasy.cms.web;

import com.fantasy.cms.bean.ArticleCategory;
import com.fantasy.cms.service.CmsService;
import com.fantasy.security.SpringSecurityUtils;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionProxy;
import org.apache.struts2.StrutsSpringJUnit4TestCase;
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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class ArticleActionTest  extends StrutsSpringJUnit4TestCase {

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
        //删除测试分类
        ArticleCategory category = cmsService.get("test");
        cmsService.remove(category.getCode());
    }

    @Test
    public void testView() throws Exception {
//        this.request.addParameter("id",articles.get(0).getId().toString());

        ActionProxy proxy = super.getActionProxy("/cms/articles/1");
        Assert.assertNotNull(proxy);
        proxy.setExecuteResult(false);

        String result = proxy.execute();
        Assert.assertEquals(Action.SUCCESS, result);
    }
}