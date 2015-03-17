package com.fantasy.security.web;

import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.security.SpringSecurityUtils;
import com.fantasy.security.bean.Job;
import com.fantasy.security.bean.Organization;
import com.fantasy.security.service.JobService;
import com.fantasy.security.service.OrganizationService;
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
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class JobActionTest extends StrutsSpringJUnit4TestCase {

    private static final Log LOG = LogFactory.getLog(OrgDimensionActionTest.class);

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private OrganizationService organizationService;//组织机构

    @Autowired
    private JobService jobService;//岗位


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

    @Test
    public void testIndex() throws Exception {
        ActionProxy proxy = super.getActionProxy("/security/job/index.do");
        Assert.assertNotNull(proxy);
        String result = proxy.execute();
        LOG.debug("testIndex--------------"+this.response.getContentAsString());
    }

    public void testSave() throws Exception {
        this.request.addParameter("name","项目经理");
        this.request.addParameter("code","xmjl001");
        this.request.addParameter("description","项目经理描述001");
        List<Organization> organizations = this.organizationService.find(new ArrayList<PropertyFilter>());
        if(!organizations.isEmpty()) {
            Organization organization = organizations.get(0);
            this.request.addParameter("organization.id",organization.getId());
            ActionProxy proxy = super.getActionProxy("/security/job/save.do");
            Assert.assertNotNull(proxy);
            //返回的数据类型
            String result = proxy.execute();
            //json数据
            LOG.debug("testSave--------------" + this.response.getContentAsString());
        }
    }

    //@Test
    public void testView() throws Exception {
        List<Job> jobs = this.jobService.find();
        if(!jobs.isEmpty()){
            Job job =jobs.get(0);
            this.request.addParameter("id",job.getId().toString());
            ActionProxy proxy = super.getActionProxy("/security/job/view.do");
            Assert.assertNotNull(proxy);
            //返回的数据类型
            String result = proxy.execute();
            //json数据
            LOG.debug("testView--------------" + this.response.getContentAsString());
        }
    }

    public void testDelete() throws Exception {
        List<Job> jobs = this.jobService.find();
        if(!jobs.isEmpty()){
            Job job =jobs.get(0);
            this.request.addParameter("ids",job.getId().toString());
            ActionProxy proxy = super.getActionProxy("/security/job/delete.do");
            Assert.assertNotNull(proxy);
            //返回的数据类型
            String result = proxy.execute();
            //json数据
            LOG.debug("testView--------------" + this.response.getContentAsString());
        }
    }
}