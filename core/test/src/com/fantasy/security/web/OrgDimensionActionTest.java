package com.fantasy.security.web;


import com.fantasy.security.SpringSecurityUtils;
import com.fantasy.security.bean.OrgDimension;
import com.fantasy.security.bean.User;
import com.fantasy.security.service.OrgDimensionService;
import com.fantasy.security.userdetails.AdminUser;
import com.fantasy.system.bean.Website;
import com.fantasy.system.service.WebsiteService;
import com.opensymphony.xwork2.ActionProxy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.StrutsSpringJUnit4TestCase;
import org.apache.struts2.views.JspSupportServlet;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class OrgDimensionActionTest extends StrutsSpringJUnit4TestCase {

    private static final Log LOG = LogFactory.getLog(OrgDimensionActionTest.class);

    @Resource
    private UserDetailsService userDetailsService;

    @Resource
    private OrgDimensionService orgDimensionService;

    @Override
    protected String getConfigPath() {
        return "struts.xml";
    }

    @Before
    public void setUp() throws Exception {
        JspSupportServlet jspSupportServlet = new JspSupportServlet();
        jspSupportServlet.init(new MockServletConfig());
        super.setUp();
        //默认登陆用户
        UserDetails userDetails = userDetailsService.loadUserByUsername("admin");
        SpringSecurityUtils.saveUserDetailsToContext(userDetails, request);
        request.getSession().setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
        LOG.debug("默认admin登陆。。。");
        testSave();
    }

    @After
    public void tearDown() throws Exception {
       //this.testDelete();

    }

    public void testSave() throws Exception{
        Website website = SpringSecurityUtils.getCurrentUser(AdminUser.class).getUser().getWebsite();
        this.request.addParameter("id","weidu1");
        this.request.addParameter("name", "维度1");
        this.request.addParameter("description", "完全不知道说的是啥");
        this.request.addParameter("website.id", website.getId().toString());
        ActionProxy proxy = super.getActionProxy("/security/orgdimension/save.do");
        Assert.assertNotNull(proxy);
        //返回的数据类型
        String result = proxy.execute();
        //json数据
        LOG.debug("testSave--------------"+this.response.getContentAsString());
    }

    public void testDelete() throws Exception{
        //传参
        List<OrgDimension> orgDimensions = this.orgDimensionService.find();
        if(!orgDimensions.isEmpty()) {
            OrgDimension orgDimension = orgDimensions.get(0);
            this.request.addParameter("ids", orgDimension.getId());
            ActionProxy proxy = super.getActionProxy("/security/orgdimension/delete.do");
            Assert.assertNotNull(proxy);
            //返回的数据类型
            String result = proxy.execute();
            //json数据
            LOG.debug("testDelete--------------" + this.response.getContentAsString());
        }
    }

    @Test
    public void testIndex() throws Exception{
        this.request.removeAllParameters();
        ActionProxy proxy = super.getActionProxy("/security/orgdimension/index.do");
        Assert.assertNotNull(proxy);
        //返回的数据类型
        String result = proxy.execute();
        //json数据
        LOG.debug("testIndex--------------"+this.response.getContentAsString());
    }

    @Test
    public void testView() throws Exception{
        List<OrgDimension> orgDimensions = this.orgDimensionService.find();
        if(!orgDimensions.isEmpty()) {
            OrgDimension orgDimension =orgDimensions.get(0);
            this.request.removeAllParameters();
            this.request.addParameter("id", orgDimension.getId());
            ActionProxy proxy = super.getActionProxy("/security/orgdimension/view.do");
            Assert.assertNotNull(proxy);
            //返回的数据类型
            String result = proxy.execute();
            //json数据
            LOG.debug("testView--------------" + this.response.getContentAsString());
        }
    }

}