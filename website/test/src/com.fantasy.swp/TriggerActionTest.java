package com.fantasy.swp;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.swp.bean.Trigger;
import com.fantasy.swp.service.TriggerService;
import com.opensymphony.xwork2.ActionProxy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.fantasy.framework.struts2.StrutsSpringJUnit4TestCase;
import org.apache.struts2.views.JspSupportServlet;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hebo on 2015/2/6.
 * 触发器保存 测试
 */
@Component
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"spring/applicationContext.xml"})
public class TriggerActionTest extends StrutsSpringJUnit4TestCase {


    private static final Log LOG = LogFactory.getLog(TriggerActionTest.class);

    @Autowired
    private TriggerService triggerService;
    @Override
    protected String getConfigPath() {
        return "struts.xml";
    }

    @Before
    public void setUp() throws Exception {
        JspSupportServlet jspSupportServlet = new JspSupportServlet();
        jspSupportServlet.init(new MockServletConfig());
        super.setUp();
    }

    @After
    public void tearDown() throws Exception {
        this.testDelete();
    }

    @Test
    public void testSave() throws Exception{
        this.request.addParameter("type","EntityChanged");
        this.request.addParameter("value","title,content");
        this.request.addParameter("description","触发器保存测试");
        ActionProxy proxy = super.getActionProxy("/swp/trigger/save.do");
        Assert.assertNotNull(proxy);
        LOG.debug("返回数据类型："+proxy.execute());
        LOG.debug("testSave返回数据："+ this.response.getContentAsString());
    }


    @Test
    public void testIndex() throws Exception {
        this.testSave();
        this.request.addParameter("EQS_value","title,content");
        ActionProxy proxy = super.getActionProxy("/swp/trigger/index.do");
        Assert.assertNotNull(proxy);
        LOG.debug("返回数据类型："+proxy.execute());
        LOG.debug("testIndex返回数据："+this.response.getContentAsString());
    }


    @Test
    public void testGet() throws Exception{
        this.testSave();
        List<PropertyFilter> filter = new ArrayList<PropertyFilter>();
        filter.add(new PropertyFilter("EQS_value","title,content"));
        Pager<Trigger> pager = triggerService.findPager(new Pager<Trigger>(1),filter);
        Assert.assertNotNull("查询数据为空",pager.getPageItems());
        this.request.addParameter("id",pager.getPageItems().get(0).getId().toString());
        ActionProxy proxy = super.getActionProxy("/swp/trigger/view.do");
        LOG.debug("返回数据类型："+proxy.execute());
        LOG.debug("testGet返回数据："+this.response.getContentAsString());
    }

    public void testDelete() throws Exception{
        List<PropertyFilter> filter = new ArrayList<PropertyFilter>();
        filter.add(new PropertyFilter("EQS_value","title,content"));
        Pager<Trigger> pager = triggerService.findPager(new Pager<Trigger>(1),filter);
        this.request.addParameter("ids",pager.getPageItems().get(0).getId().toString());
        ActionProxy proxy = super.getActionProxy("/swp/trigger/delete.do");
        LOG.debug("返回数据类型："+proxy.execute());
        LOG.debug("testDelete返回数据："+this.response.getContentAsString());
    }




}
