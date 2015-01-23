package com.fantasy.security.web;

import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.security.SpringSecurityUtils;
import com.fantasy.security.bean.OrgDimension;
import com.fantasy.security.bean.OrgHelpBean;
import com.fantasy.security.bean.Organization;
import com.fantasy.security.service.OrgDimensionService;
import com.fantasy.security.service.OrganizationService;
import com.opensymphony.xwork2.ActionProxy;
import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.StrutsSpringJUnit4TestCase;
import org.apache.struts2.views.JspSupportServlet;
import org.hibernate.criterion.Restrictions;
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
public class OrganizActionTest extends StrutsSpringJUnit4TestCase {


    private static final Log LOG = LogFactory.getLog(OrgDimensionActionTest.class);

    @Resource
    private UserDetailsService userDetailsService;

    @Resource
    private OrgDimensionService orgDimensionService;

    @Resource
    private OrganizationService organizationService;


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
        //testSave();
    }

    @After
    public void tearDown() throws Exception {
        //this.testDelete();

    }

    //@Test
    public void testIndex() throws Exception {
        this.request.removeAllParameters();
        this.request.addParameter("EQS_orgDimension.id","weidu001");
        ActionProxy proxy = super.getActionProxy("/security/organize/index.do");
        Assert.assertNotNull(proxy);
        //返回的数据类型
        String result = proxy.execute();
        //json数据
        LOG.debug("testIndex--------------"+this.response.getContentAsString());
    }

    public void testSave() throws Exception {
        //组织机构 数据
        this.request.addParameter("id","jg00002");
        this.request.addParameter("name","机构00002");
        this.request.addParameter("description","机构00002");
        this.request.addParameter("type","company");

        //对应组织维度 与上级组织机构
        this.request.addParameter("orgHelpBeans[0].orgDimension.id","weidu001");
        //上级组织机构
        this.request.addParameter("orgHelpBeans[0].organization.id","jg0001");
        //this.request.addParameter("orgHelpBeans[1].orgDimension.id","weidu002");
        //this.request.addParameter("orgHelpBeans[1].organization.id","jg002");
        ActionProxy proxy = super.getActionProxy("/security/organize/save.do");
        Assert.assertNotNull(proxy);
        //返回的数据类型
        String result = proxy.execute();
        //json数据
        LOG.debug("testSave--------------"+this.response.getContentAsString());
    }

    @Test
    public void testSearch() throws Exception{
        ActionProxy proxy = super.getActionProxy("/security/organize/search.do");
        Assert.assertNotNull(proxy);
        //返回的数据类型
        String result = proxy.execute();
        //json数据
        LOG.debug("testSearch--------------"+this.response.getContentAsString());
    }


    //@Test
    public void testView() throws Exception {
        this.request.removeAllParameters();
        this.request.addParameter("id","jg002");
        ActionProxy proxy = super.getActionProxy("/security/organize/view.do");
        Assert.assertNotNull(proxy);
        //返回的数据类型
        String result = proxy.execute();
        //json数据
        LOG.debug("testView--------------"+this.response.getContentAsString());
    }

    /**
     * 删除全部组织机构 关系也删除
     * @throws Exception
     */
    public void testAllDelete() throws Exception{
        List<Organization> organizations = this.organizationService.find(new ArrayList<PropertyFilter>());
        for(Organization organization:organizations){
            this.organizationService.delete(organization.getId());
        }
    }

    public void testDelete() throws Exception {
        this.request.removeAllParameters();
        this.request.addParameter("ids","jg002");
        ActionProxy proxy = super.getActionProxy("/security/organize/delete.do");
        Assert.assertNotNull(proxy);
        //返回的数据类型
        String result = proxy.execute();
        //json数据
        LOG.debug("testDelete--------------"+this.response.getContentAsString());

    }
}