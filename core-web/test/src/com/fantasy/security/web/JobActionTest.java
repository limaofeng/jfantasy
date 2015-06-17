package com.fantasy.security.web;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.struts2.StrutsSpringJUnit4TestCase;
import com.fantasy.security.SpringSecurityUtils;
import com.fantasy.security.bean.Job;
import com.fantasy.security.bean.OrgDimension;
import com.fantasy.security.bean.OrgHelpBean;
import com.fantasy.security.bean.Organization;
import com.fantasy.security.service.JobService;
import com.fantasy.security.service.OrgDimensionService;
import com.fantasy.security.service.OrganizationService;
import com.fantasy.security.userdetails.AdminUser;
import com.fantasy.system.bean.Website;
import com.opensymphony.xwork2.ActionProxy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.views.JspSupportServlet;
import org.hibernate.criterion.Restrictions;
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

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class JobActionTest extends StrutsSpringJUnit4TestCase {

    private static final Log LOG = LogFactory.getLog(OrgDimensionActionTest.class);

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private OrganizationService organizationService;//组织机构

    @Autowired
    private OrgDimensionService orgDimensionService;//组织维度

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
        //创建维度
        Website website = SpringSecurityUtils.getCurrentUser(AdminUser.class).getUser().getWebsite();
        OrgDimension orgDimension = new OrgDimension();
        orgDimension.setId("Testweidu");
        orgDimension.setName("维度测试");
        orgDimension.setDescription("维度测试");
        orgDimension.setWebsite(website);
        this.orgDimensionService.save(orgDimension);
        //添加组织机构
        Organization organization = new Organization();
        organization.setId("Testzhuzhi");
        organization.setName("测试组织机构");
        organization.setDescription("测试组织机构");
        organization.setType(Organization.OrgType.company);
        List<OrgHelpBean> orgHelpBeans = new ArrayList<OrgHelpBean>();
        OrgHelpBean orgHelpBean = new OrgHelpBean();
        orgHelpBean.setOrgDimension(this.orgDimensionService.findUnique(Restrictions.eq("id",orgDimension.getId())));
        orgHelpBeans.add(orgHelpBean);
        organization.setOrgHelpBeans(orgHelpBeans);
        this.organizationService.save(organization);
        //创建岗位
        Job job = new Job();
        job.setCode("TestJob");
        job.setName("测试岗位");
        job.setDescription("测试岗位");
        job.setOrganization(organization);
        this.jobService.save(job);
    }

    @After
    public void tearDown() throws Exception {
        this.organizationService.delete("Testzhuzhi");
        this.orgDimensionService.delete("Testweidu");
        Job job = this.jobService.findUnique("TestJob");
        if(job!=null){
            this.jobService.delete(job.getId());
        }

    }

    @Test
    public void testSearch() throws Exception {
        this.request.setMethod("GET");
        this.request.addParameter("EQS_code","TestJob");
        ActionProxy proxy = super.getActionProxy("/api/0.1/security/jobs");
        Assert.assertNotNull(proxy);
        LOG.debug("返回数据类型：" + proxy.execute());
        LOG.debug("testSearch返回数据："+this.response.getContentAsString());
    }


    @Test
    public void testSave() throws Exception {
        this.request.setMethod("POST");
        this.request.addParameter("name","项目经理");
        this.request.addParameter("code","xmjl001");
        this.request.addParameter("description","项目经理描述001");
        Organization organization = this.organizationService.findUnique(Restrictions.eq("id","Testzhuzhi"));
        this.request.addParameter("organization.id",organization.getId());
        ActionProxy proxy = super.getActionProxy("/api/0.1/security/jobs/");
        LOG.debug("返回数据类型：" + proxy.execute());
        LOG.debug("testSave返回数据：" + this.response.getContentAsString());
    }

    @Test
    public void testUpdate() throws Exception {
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        filters.add(new PropertyFilter("EQS_code","TestJob"));
        Pager<Job> pager = this.jobService.findPager(new Pager<Job>(1),filters);
        if(!pager.getPageItems().isEmpty()){
            Job job = pager.getPageItems().get(0);
            this.request.setMethod("PUT");
            this.request.addParameter("id",job.getId().toString());
            this.request.addParameter("name","项目经理1");
            this.request.addParameter("code","TestJob");
            this.request.addParameter("description","update测试");
            Organization organization = this.organizationService.findUnique(Restrictions.eq("id","Testzhuzhi"));
            this.request.addParameter("organization.id",organization.getId());
            ActionProxy proxy = super.getActionProxy("/api/0.1/security/jobs/"+job.getId());
            String result =  proxy.execute();
            LOG.debug("返回数据类型：" + result);
            LOG.debug("testUpdate返回数据："+this.response.getContentAsString());
        }

    }

    @Test
    public void testView() throws Exception {
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        filters.add(new PropertyFilter("EQS_code","TestJob"));
        Pager<Job> pager = this.jobService.findPager(new Pager<Job>(1),filters);
        if(!pager.getPageItems().isEmpty()){
            this.request.setMethod("GET");
            ActionProxy proxy = super.getActionProxy("/api/0.1/security/jobs/"+pager.getPageItems().get(0).getId());
            LOG.debug("返回数据类型：" + proxy.execute());
            LOG.debug("testView返回数据："+this.response.getContentAsString());
        }

    }

    @Test
    public void testDelete() throws Exception {
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        filters.add(new PropertyFilter("EQS_code","TestJob"));
        Pager<Job> pager = this.jobService.findPager(new Pager<Job>(1),filters);
        if(!pager.getPageItems().isEmpty()){
            this.request.setMethod("DELETE");
            ActionProxy proxy = super.getActionProxy("/api/0.1/security/jobs/"+pager.getPageItems().get(0).getId());
            LOG.debug("返回数据类型：" + proxy.execute());
            LOG.debug("testDelete返回数据："+this.response.getContentAsString());
        }

    }
}