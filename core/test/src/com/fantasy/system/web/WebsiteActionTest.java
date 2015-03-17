package com.fantasy.system.web;

import com.fantasy.file.bean.FileManagerConfig;
import com.fantasy.file.service.FileManagerService;
import com.fantasy.framework.struts2.ActionSupport;
import com.fantasy.framework.struts2.context.ActionConstants;
import com.fantasy.security.SpringSecurityUtils;
import com.fantasy.system.bean.Website;
import com.fantasy.system.service.WebsiteService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@Component
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class WebsiteActionTest extends StrutsSpringJUnit4TestCase {

    private final static Log LOG = LogFactory.getLog(WebsiteActionTest.class);

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private WebsiteService websiteService;
    @Autowired
    private FileManagerService fileManagerService;

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
        Website website = this.websiteService.get("test");
        if (website != null) {
            this.websiteService.delete(website.getId());
        }
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
        this.request.addHeader("X-Requested-With", "XMLHttpRequest");
        this.request.addParameter("key", "");
        this.request.addParameter("name", "测试站点");
        this.request.addParameter("web", "http://test.jfantasy.org");
        this.request.addParameter("rootMenu.id", "");
        this.request.addParameter("defaultFileManager.id", "haolue-default");
        this.request.addParameter("defaultUploadFileManager.id", "haolue-upload");

        ActionProxy proxy = super.getActionProxy("/system/website/save.do");
        Assert.assertNotNull(proxy);

        String result = proxy.execute();

        LOG.debug("result:" + this.response.getContentAsString());

        if (proxy.getAction() instanceof ActionSupport) {
            List<String> messages = ((ActionSupport) proxy.getAction()).getFieldErrors().get("key");
            Assert.assertNotNull(messages);
            LOG.debug("messages:" + Arrays.toString(messages.toArray(new String[messages.size()])));
        }

        Assert.assertEquals(ActionConstants.JSONDATA, result);

        this.request.removeAllParameters();

        this.request.addParameter("key", "test");
        this.request.addParameter("name", "测试站点");
        this.request.addParameter("web", "http://test.jfantasy.org");
        this.request.addParameter("rootMenu.id", "");
        this.request.addParameter("defaultFileManager.id", "haolue-default");
        this.request.addParameter("defaultUploadFileManager.id", "haolue-upload");

        proxy = super.getActionProxy("/system/website/save.do");
        result = proxy.execute();

        LOG.debug("result:" + this.response.getContentAsString());

        Assert.assertEquals(ActionConstants.JSONDATA, result);
    }

    @Test
    public void testView() throws Exception {
        Website website = new Website();
        website.setKey("test");
        website.setName("测试站点");
        website.setWeb("http://test.jfantasy.org");
        website.setDefaultFileManager(new FileManagerConfig("haolue-default"));
        website.setDefaultUploadFileManager(new FileManagerConfig("haolue-upload"));
        this.websiteService.save(website);

        this.request.addParameter("id", website.getId().toString());

        ActionProxy proxy = super.getActionProxy("/system/website/view.do");
        Assert.assertNotNull(proxy);

        String result = proxy.execute();

        LOG.debug("result:" + this.response.getContentAsString());

        Assert.assertEquals(ActionConstants.JSONDATA, result);
    }

    @Test
    public void testDelete() throws Exception {
        this.testSave();
        this.request.removeAllParameters();

        this.request.addParameter("ids", this.websiteService.get("test").getId().toString());

        ActionProxy proxy = super.getActionProxy("/system/website/delete.do");
        Assert.assertNotNull(proxy);

        String result = proxy.execute();

        LOG.debug("result:" + this.response.getContentAsString());

        Assert.assertEquals(ActionConstants.JSONDATA, result);
    }
}