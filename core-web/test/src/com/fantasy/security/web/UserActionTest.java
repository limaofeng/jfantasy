package com.fantasy.security.web;


import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.struts2.StrutsSpringJUnit4TestCase;
import com.fantasy.security.SpringSecurityUtils;
import com.fantasy.security.bean.Role;
import com.fantasy.security.bean.User;
import com.fantasy.security.bean.UserDetails;
import com.fantasy.security.service.UserService;
import com.fantasy.system.service.WebsiteService;
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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class UserActionTest extends StrutsSpringJUnit4TestCase {

    private static final Log LOG = LogFactory.getLog(UserActionTest.class);

    @Autowired
    private UserService userService;

    @Autowired
    private WebsiteService websiteService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected String getConfigPath() {
        return "struts.xml";
    }

    @Before
    public void setUp() throws Exception {
        this.tearDown();
        JspSupportServlet jspSupportServlet = new JspSupportServlet();
        jspSupportServlet.init(new MockServletConfig());
        super.setUp();
        //默认登陆用户
        org.springframework.security.core.userdetails.UserDetails userDetails = userDetailsService.loadUserByUsername("admin");
        SpringSecurityUtils.saveUserDetailsToContext(userDetails, request);
        request.getSession().setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
        LOG.debug("默认admin登陆。。。");
        User user = new User();
        user.setUsername("hhh001");
        user.setNickName("呵呵");
        user.setPassword("123456");
        List<Role> roleList = new ArrayList<Role>();
        Role role = new Role();
        role.setCode("SYSTEM");
        roleList.add(role);
        user.setRoles(roleList);
        UserDetails details = new UserDetails();
        details.setEmail("393469668@qq.com");
        details.setDescription("测试");
        user.setDetails(details);
        user.setWebsite(websiteService.get(7L));
        this.userService.save(user);

    }

    @After
    public void tearDown() throws Exception {
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        filters.add(new PropertyFilter("EQS_username","hhh001"));
        Pager<User> pager = this.userService.findPager(new Pager<User>(1),filters);
        if(!pager.getPageItems().isEmpty()){
            this.userService.delete(pager.getPageItems().get(0).getId());
        }

        List<PropertyFilter> userfilters = new ArrayList<PropertyFilter>();
        userfilters.add(new PropertyFilter("EQS_username","hhh002"));
        Pager<User> userpager = this.userService.findPager(new Pager<User>(1),userfilters);
        if(!userpager.getPageItems().isEmpty()){
            this.userService.delete(userpager.getPageItems().get(0).getId());
        }
    }

    @Test
    @Transactional
    public void testSearch() throws Exception {
        this.request.setMethod("GET");
        this.request.addParameter("EQS_username","hhh001");
        this.request.addParameter("pager.pageSize","100");
        ActionProxy proxy = super.getActionProxy("/api/0.1/security/users");
        Assert.assertNotNull(proxy);
        LOG.debug("返回数据类型：" + proxy.execute());
        LOG.debug("testSearch返回数据："+this.response.getContentAsString());
        Assert.assertEquals(response.getStatus(),200);
    }

    @Test
    public void testCreate() throws Exception{
        this.request.setMethod("POST");
        this.request.addParameter("username","hhh002");
        this.request.addParameter("password","123456");
        this.request.addParameter("nickName","hebo");
        this.request.addParameter("enabled","true");
        this.request.addParameter("roles[0].code","SYSTEM");
        this.request.addParameter("details.email","393469668@qq.com");
        this.request.addParameter("website.id","7");
        ActionProxy proxy = super.getActionProxy("/api/0.1/security/users/");
        LOG.debug("返回数据类型：" + proxy.execute());
        LOG.debug("testSave返回数据：" + this.response.getContentAsString());
    }

    @Test
    public void testUpdate() throws Exception{
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        filters.add(new PropertyFilter("EQS_username","hhh001"));
        Pager<User> pager = this.userService.findPager(new Pager<User>(1),filters);
        if(!pager.getPageItems().isEmpty()){
            this.request.setMethod("PUT");
            this.request.addParameter("id",pager.getPageItems().get(0).getId().toString());
            this.request.addParameter("username","hhh001");
            this.request.addParameter("password","123456");
            this.request.addParameter("nickName","hebo11");
            this.request.addParameter("enabled","true");
            this.request.addParameter("roles[0].code","SYSTEM");
            this.request.addParameter("details.email","393469668@qq.com");
            this.request.addParameter("website.id","7");
            ActionProxy proxy = super.getActionProxy("/api/0.1/security/users/"+pager.getPageItems().get(0).getId());
            LOG.debug("返回数据类型：" + proxy.execute());
            LOG.debug("testUpdate返回数据：" + this.response.getContentAsString());
        }
    }

    @Test
    public void testView() throws Exception{
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        filters.add(new PropertyFilter("EQS_username","hhh001"));
        Pager<User> pager = this.userService.findPager(new Pager<User>(1),filters);
        if(!pager.getPageItems().isEmpty()){
            this.request.setMethod("GET");
            ActionProxy proxy = super.getActionProxy("/api/0.1/security/users/hhh001");
            LOG.debug("返回数据类型："+proxy.execute());
            LOG.debug("testView返回数据："+this.response.getContentAsString());
            Assert.assertEquals(response.getStatus(),200);
        }

    }

    @Test
    public void testDelete() throws Exception{
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        filters.add(new PropertyFilter("EQS_username","hhh001"));
        Pager<User> pager = this.userService.findPager(new Pager<User>(1),filters);
        if(!pager.getPageItems().isEmpty()){
            this.request.setMethod("DELETE");
            ActionProxy proxy = super.getActionProxy("/api/0.1/security/users/"+pager.getPageItems().get(0).getId());
            LOG.debug("返回数据类型：" + proxy.execute());
            LOG.debug("testDelete返回数据："+this.response.getContentAsString());
        }
    }
}
