package com.fantasy.cms.web;

import com.fantasy.cms.bean.Article;
import com.fantasy.cms.bean.ArticleCategory;
import com.fantasy.cms.service.CmsService;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.struts2.StrutsSpringJUnit4TestCase;
import com.fantasy.framework.util.common.DateUtil;
import com.fantasy.framework.util.common.JdbcUtil;
import com.fantasy.security.SpringSecurityUtils;
import com.opensymphony.xwork2.ActionProxy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
@Transactional
public class ArticleActionTest extends StrutsSpringJUnit4TestCase {

    private static final Log LOG = LogFactory.getLog(ArticleActionTest.class);

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

        ArticleCategory category = this.cmsService.get("root");
        if (category == null) {
            category = new ArticleCategory();
            category.setCode("root");
            category.setName("JUnit测试");
            category.setDescription("root");
            cmsService.save(category);
        }

        this.request.removeAllParameters();

        this.request.setMethod("POST");
        this.request.addParameter("title", "测试文章标题");
        this.request.addParameter("summary", "测试文章摘要");
        this.request.addParameter("keywords", "test");
        this.request.addParameter("content", "测试文章正文");
        this.request.addParameter("author", "limaofeng");
        this.request.addParameter("releaseDate", DateUtil.format("yyyy-MM-dd"));
        this.request.addParameter("category.code", "root");
        this.request.addParameter("issue", "true");
        ActionProxy proxy = super.getActionProxy("/cms/articles/");
        LOG.debug("返回数据类型：" + proxy.execute());
        LOG.debug("testSave返回数据：" + this.response.getContentAsString());

        this.response.setCommitted(false);
        this.response.reset();
        this.request.removeAllParameters();
    }

    @After
    public void tearDown() throws Exception {
        this.request.removeAllParameters();

        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        filters.add(new PropertyFilter("EQS_title", "测试文章标题"));
        Pager<Article> pager = this.cmsService.findPager(new Pager<Article>(1), filters);
        this.request.setMethod("DELETE");
        ActionProxy proxy = super.getActionProxy("/cms/articles/" + pager.getPageItems().get(0).getId());
        LOG.debug("返回数据类型：" + proxy.execute());
        LOG.debug("testDelete返回数据：" + this.response.getContentAsString());

        this.request.removeAllParameters();
        this.response.setCommitted(false);
        this.response.reset();
    }

    @Test
    public void testView() throws Exception {

        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        filters.add(new PropertyFilter("EQS_title", "测试文章标题"));
        Pager<Article> pager = this.cmsService.findPager(new Pager<Article>(1), filters);
        this.request.setMethod("GET");
        final ActionProxy proxy = super.getActionProxy("/cms/articles/" + pager.getPageItems().get(0).getId());
        LOG.debug("返回数据类型：" + JdbcUtil.transaction(new JdbcUtil.Callback<String>() {

            @Override
            public String run() {
                try {
                    return proxy.execute();
                } catch (Exception e) {
                    LOG.error(e.getMessage(), e);
                    return null;
                }
            }
        }, TransactionDefinition.PROPAGATION_REQUIRED));
        LOG.debug("testView返回数据：" + this.response.getContentAsString());
    }

}