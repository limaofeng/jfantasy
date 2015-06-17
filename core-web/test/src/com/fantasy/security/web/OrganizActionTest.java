package com.fantasy.security.web;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.struts2.StrutsSpringJUnit4TestCase;
import com.fantasy.security.SpringSecurityUtils;
import com.fantasy.security.bean.OrgDimension;
import com.fantasy.security.bean.Organization;
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
public class OrganizActionTest extends StrutsSpringJUnit4TestCase {


    private static final Log LOG = LogFactory.getLog(OrgDimensionActionTest.class);

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private OrgDimensionService orgDimensionService;//组织维度
    @Autowired
    private OrganizationService organizationService;//组织机构

    @Override
    protected String getConfigPath() {
        return "struts.xml";
    }

    @Before
    public void setUp() throws Exception {
        /* orgDimensionActionTest.setUp();*/
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
        //创建组织结构
        Organization organization = new Organization();
        organization.setId("TestID");
        organization.setName("测试机构");
        organization.setDescription("测试机构");
        organization.setType(Organization.OrgType.company);
        this.organizationService.save(organization);
    }

    @After
    public void tearDown() throws Exception {
         Organization organization = this.organizationService.findUnique(Restrictions.eq("id","TestID"));
        if(organization!=null){
            this.organizationService.delete(organization.getId());
        }
        //最后删除维度和组织机构之间的关系
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        filters.add(new PropertyFilter("EQS_id","Testweidu"));
        Pager<OrgDimension> pager = this.orgDimensionService.findPager(new Pager<OrgDimension>(1),filters);
        if(!pager.getPageItems().isEmpty()){
            this.orgDimensionService.delete(pager.getPageItems().get(0).getId());
        }



    }


    private void testView(List<Organization> organizations,int layer){
        layer++;
        for(Organization organization:organizations){
            LOG.debug("层级----"+layer+"-------"+organization.getName());
            testView(organization.getChildren(), layer);
        }
    }


    @Test
    public void testCreate() throws Exception {
        this.request.setMethod("POST");
        this.request.addParameter("id", "TestID");
        this.request.addParameter("name", "机构1");
        this.request.addParameter("description", "机构1");
        this.request.addParameter("type", "company");
        //对应组织维度 与上级组织机构
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        filters.add(new PropertyFilter("EQS_id","Testweidu"));
        Pager<OrgDimension> pager = this.orgDimensionService.findPager(new Pager<OrgDimension>(1),filters);
        if(!pager.getPageItems().isEmpty()) {
            this.request.addParameter("orgHelpBeans[0].orgDimension.id", pager.getPageItems().get(0).getId());//添加维度
            ActionProxy proxy = super.getActionProxy("/api/0.1/security/organizations/");
            LOG.debug("返回数据类型：" + proxy.execute());
            LOG.debug("testSave返回数据：" + this.response.getContentAsString());
            Assert.assertEquals(response.getStatus(),200);
        }
    }

    @Test
    public void testUpdate() throws Exception {
        this.request.setMethod("PUT");
        this.request.addParameter("id", "TestID");
        this.request.addParameter("name", "测试机构");
        this.request.addParameter("description", "测试机构");
        this.request.addParameter("type", "company");
        //对应组织维度 与上级组织机构
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        filters.add(new PropertyFilter("EQS_id","Testweidu"));
        Pager<OrgDimension> pager = this.orgDimensionService.findPager(new Pager<OrgDimension>(1),filters);
        if(!pager.getPageItems().isEmpty()) {
            this.request.addParameter("orgHelpBeans[0].orgDimension.id", pager.getPageItems().get(0).getId());//添加维度
            ActionProxy proxy = super.getActionProxy("/api/0.1/security/organizations/TestID");
            LOG.debug("返回数据类型：" + proxy.execute());
            LOG.debug("testUpdate返回数据：" + this.response.getContentAsString());
            Assert.assertEquals(response.getStatus(),200);
        }
    }

    @Test
    public void testSearch() throws Exception {
        this.request.setMethod("GET");
        this.request.addParameter("EQS_id","TestID");
        ActionProxy proxy = super.getActionProxy("/api/0.1/security/organizations");
        LOG.debug("返回数据类型：" + proxy.execute());
        LOG.debug("testSearch返回数据：" + this.response.getContentAsString());
        Assert.assertEquals(response.getStatus(),200);
    }


    @Test
    public void testView() throws Exception {
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        filters.add(new PropertyFilter("EQS_id","TestID"));
        Pager<Organization> pager = this.organizationService.findPager(new Pager<Organization>(1),filters);
        if(!pager.getPageItems().isEmpty()){
            this.request.removeAllParameters();
            this.response.setCommitted(false);
            this.response.reset();
            this.request.setMethod("GET");
            ActionProxy proxy = super.getActionProxy("/api/0.1/security/organizations/"+pager.getPageItems().get(0).getId());
            LOG.debug("返回数据类型："+proxy.execute());
            LOG.debug("testView返回数据："+this.response.getContentAsString());
            Assert.assertEquals(response.getStatus(),200);
        }

    }

    /**
     * 删除全部组织机构 关系也删除
     *
     * @throws Exception
     */
    public void testAllDelete() throws Exception {
        List<Organization> organizations = this.organizationService.find(new ArrayList<PropertyFilter>());
        for (Organization organization : organizations) {
            this.organizationService.delete(organization.getId());
        }
    }


    @Test
    public void testDelete() throws Exception {
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        filters.add(new PropertyFilter("EQS_id","TestID"));
        Pager<Organization> pager = this.organizationService.findPager(new Pager<Organization>(1),filters);
        if(!pager.getPageItems().isEmpty()){
            this.request.setMethod("DELETE");
            ActionProxy proxy = super.getActionProxy("/api/0.1/security/organizations/"+pager.getPageItems().get(0).getId());
            LOG.debug("返回数据类型："+proxy.execute());
            LOG.debug("testDelete返回数据："+this.response.getContentAsString());
            Assert.assertEquals(response.getStatus(),200);
         }
    }
}