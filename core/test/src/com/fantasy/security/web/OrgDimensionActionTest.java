package com.fantasy.security.web;


import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.struts2.StrutsSpringJUnit4TestCase;
import com.fantasy.security.SpringSecurityUtils;
import com.fantasy.security.bean.OrgDimension;
import com.fantasy.security.service.OrgDimensionService;
import com.fantasy.security.userdetails.AdminUser;
import com.fantasy.system.bean.Website;
import com.opensymphony.xwork2.ActionProxy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.views.JspSupportServlet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
@Component
public class OrgDimensionActionTest extends StrutsSpringJUnit4TestCase {

    private static final Log LOG = LogFactory.getLog(OrgDimensionActionTest.class);

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
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
       this.testDelete();
    }


    public void testSave() throws Exception{
       /* this.request.removeAllParameters();
        this.response.setCommitted(false);
        this.response.reset();*/

        Website website = SpringSecurityUtils.getCurrentUser(AdminUser.class).getUser().getWebsite();
        this.request.setMethod("POST");
        this.request.addParameter("id","weidu1");
        this.request.addParameter("name", "维度1");
        this.request.addParameter("description", "完全不知道说的是啥");
        this.request.addParameter("website.id", website.getId().toString());
        ActionProxy proxy = super.getActionProxy("/security/orgdimensions/");
        LOG.debug("返回数据类型："+proxy.execute());
        LOG.debug("testDelete返回数据："+this.response.getContentAsString());
    }

    public void testDelete() throws Exception{
        this.request.removeAllParameters();
        this.response.setCommitted(false);
        this.response.reset();
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        filters.add(new PropertyFilter("EQS_name","维度1"));
        Pager<OrgDimension> pager = this.orgDimensionService.findPager(new Pager<OrgDimension>(1),filters);
        if(!pager.getPageItems().isEmpty()){
            this.request.setMethod("DELETE");
            ActionProxy proxy = super.getActionProxy("/security/orgdimensions/"+pager.getPageItems().get(0).getId());
            LOG.debug("返回数据类型："+proxy.execute());
            LOG.debug("testDelete返回数据："+this.response.getContentAsString());
        }
    }

    @Test
    public void testSearch() throws Exception{
        this.request.removeAllParameters();
        this.response.setCommitted(false);
        this.response.reset();
        this.request.addParameter("EQS_id","weidu1");
        this.request.setMethod("GET");
        ActionProxy proxy = super.getActionProxy("/security/orgdimensions");
        LOG.debug("返回数据类型："+proxy.execute());
        LOG.debug("testSearch返回数据："+this.response.getContentAsString());
    }

    @Test
    public void testView() throws Exception{
        this.request.removeAllParameters();
        this.response.setCommitted(false);
        this.response.reset();
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        filters.add(new PropertyFilter("EQS_id","weidu1"));
        Pager<OrgDimension> pager = this.orgDimensionService.findPager(new Pager<OrgDimension>(1),filters);
        if(!pager.getPageItems().isEmpty()){
            this.request.setMethod("GET");
            ActionProxy proxy = super.getActionProxy("/security/orgdimensions/"+pager.getPageItems().get(0).getId());
            LOG.debug("返回数据类型："+proxy.execute());
            LOG.debug("testView返回数据："+this.response.getContentAsString());
        }

    }

}