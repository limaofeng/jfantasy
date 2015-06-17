package com.fantasy.security.web;

import com.fantasy.framework.struts2.StrutsSpringJUnit4TestCase;
import com.fantasy.security.SpringSecurityUtils;
import com.opensymphony.xwork2.ActionProxy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.views.JspSupportServlet;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class OrgRelationActionTest extends StrutsSpringJUnit4TestCase {

    private static final Log LOG = LogFactory.getLog(OrgRelationActionTest.class);

    @Autowired
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
    public void testSearch() throws Exception {
        this.request.setMethod("GET");
        this.request.addParameter("EQS_orgDimension.id","default");
        this.request.addParameter("pager.pageSize","100");
        ActionProxy proxy = super.getActionProxy("/api/0.1/security/orgrelations");
        Assert.assertNotNull(proxy);
        LOG.debug("返回数据类型：" + proxy.execute());
        LOG.debug("testSearch返回数据："+this.response.getContentAsString());
        Assert.assertEquals(response.getStatus(),200);
    }
}
