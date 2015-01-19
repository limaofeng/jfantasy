package com.fantasy.system.web;

import com.fantasy.framework.struts2.context.ActionConstants;
import com.fantasy.security.SpringSecurityUtils;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionProxy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.StrutsSpringJUnit4TestCase;
import org.apache.struts2.views.JspSupportServlet;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class WebsiteActionTest extends StrutsSpringJUnit4TestCase {

    private final static Log LOG = LogFactory.getLog(WebsiteActionTest.class);

    @Resource
    private UserDetailsService userDetailsService;

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
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testIndex() throws Exception {
        ActionProxy proxy = super.getActionProxy("/system/website/index.do");
        Assert.assertNotNull(proxy);

        String result = proxy.execute();

        LOG.debug("result:" + this.response.getContentAsString());

        Assert.assertEquals(Action.SUCCESS, result);
    }

    @Test
    public void testSearch() throws Exception {
        ActionProxy proxy = super.getActionProxy("/system/website/search.do");
        Assert.assertNotNull(proxy);

        String result = proxy.execute();

        LOG.debug("result:" + this.response.getContentAsString());

        Assert.assertEquals(ActionConstants.JSONDATA, result);
    }

    @Test
    public void testSave() throws Exception {

    }

    @Test
    public void testEdit() throws Exception {

    }

    @Test
    public void testDelete() throws Exception {

    }
}