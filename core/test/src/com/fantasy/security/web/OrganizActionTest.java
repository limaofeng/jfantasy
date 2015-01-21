package com.fantasy.security.web;

import com.fantasy.security.SpringSecurityUtils;
import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.StrutsSpringJUnit4TestCase;
import org.apache.struts2.views.JspSupportServlet;
import org.junit.After;
import org.junit.Before;
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
public class OrganizActionTest extends StrutsSpringJUnit4TestCase {


    private static final Log LOG = LogFactory.getLog(OrgDimensionActionTest.class);

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
        testSave();
    }

    @After
    public void tearDown() throws Exception {
        this.testDelete();

    }

    public void testSearch() throws Exception {

    }

    public void testSave() throws Exception {
        this.request.addParameter("id", "jg001");
        this.request.addParameter("name","机构001");
        this.request.addParameter("type","company");
        this.request.addParameter("description","机构001");

    }

    public void testView() throws Exception {

    }

    public void testDelete() throws Exception {

    }
}