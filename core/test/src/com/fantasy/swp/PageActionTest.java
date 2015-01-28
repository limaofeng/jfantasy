package com.fantasy.swp;

import com.fantasy.file.FileManager;
import com.fantasy.file.manager.LocalFileManager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.swp.bean.Data;
import com.fantasy.swp.bean.DataInferface;
import com.fantasy.swp.bean.Page;
import com.fantasy.swp.bean.Template;
import com.fantasy.swp.service.DataInferfaceService;
import com.fantasy.swp.service.DataService;
import com.fantasy.swp.service.TemplateService;
import com.fantasy.swp.service._PageService;
import com.fantasy.system.bean.Website;
import com.fantasy.system.service.WebsiteService;
import com.fantasy.system.web.WebsiteActionTest;
import com.opensymphony.xwork2.ActionProxy;
import org.apache.struts2.StrutsSpringJUnit4TestCase;
import org.apache.struts2.views.JspSupportServlet;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by wml on 2015/1/25.
 */
@Component
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class PageActionTest extends StrutsSpringJUnit4TestCase {

    @Resource
    private TemplateActionTest templeateTest;
    @Resource
    private WebsiteActionTest websiteActionTest;
    @Resource
    private TemplateService templateService;
    @Resource
    private WebsiteService websiteService;
    @Resource
    private DataInferfaceService dataInferfaceService;
    @Resource
    private DataActionTest dataActionTest;
    @Resource
    private DataService dataService;
    @Resource
    private _PageService pageService;

    @Before
    public void setUp() throws Exception {
        JspSupportServlet jspSupportServlet = new JspSupportServlet();
        jspSupportServlet.init(new MockServletConfig());
        super.setUp();

        templeateTest.setUp();
        websiteActionTest.setUp();
        dataActionTest.setUp();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testSave() throws Exception {
        this.request.addHeader("X-Requested-With", "XMLHttpRequest");
        this.request.addParameter("name", "PAGE_JUNIT_TEST");
        this.request.addParameter("path", "/template/test.html");
        // 模版
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        filters.add(new PropertyFilter("EQS_name","ARTICLE_JUNIT_TEST"));
        List<Template> templates = templateService.find(filters);
        Long templateId;
        if(templates==null || templates.size()<=0){
            templeateTest.testSave();
            templates = templateService.find(filters);
            templateId = templates.get(0).getId();
        }else{
            templateId = templates.get(0).getId();
        }
        this.request.addParameter("template.id", templateId+"");

        //测试站点
        Website website = this.websiteService.get("test");
        if(website==null){
            websiteActionTest.testSave();
            website = this.websiteService.get("test");
        }
        this.request.addParameter("webSite.id", website.getId()+"");

        List<PropertyFilter> dataIfilters = new ArrayList<PropertyFilter>();
        dataIfilters.add(new PropertyFilter("EQL_template.id",templateId+""));
        List<DataInferface> dataInferfaces = this.dataInferfaceService.find(dataIfilters);
        for(int i=0; i<dataInferfaces.size(); i++){
            Long id = dataInferfaces.get(i).getId();
            List<PropertyFilter> datafilters = new ArrayList<PropertyFilter>();
            datafilters.add(new PropertyFilter("EQL_dataInferface.id",id+""));
            List<Data> datas = dataService.find(datafilters);
            if(datas.size()<=0){
                dataActionTest.testSave();
                datas = dataService.find(datafilters);
            }
            this.request.addParameter("datas["+i+"].id", datas.get(0).getId()+"");
        }
        this.request.addParameter("pageType", "list");
        this.request.addParameter("pageSize", "3");
        ActionProxy proxy = super.getActionProxy("/swp/page/save.do");
        String result = proxy.execute();
        System.out.println("result=" + result);
    }

    @Test
    public void testDelete() throws Exception {
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        filters.add(new PropertyFilter("EQS_name","PAGE_JUNIT_TEST"));
        List<Page> pages = this.pageService.find(filters);
        if(pages==null || pages.size()<=0){
            this.testSave();
            pages = this.pageService.find(filters);
        }
        Assert.assertNotNull(pages);
        for(int i=0; i<pages.size(); i++){
            this.request.addParameter("ids", pages.get(i).getId()+"");
        }
        ActionProxy proxy = super.getActionProxy("/swp/page/delete.do");
        Assert.assertNotNull(proxy);

        String result = proxy.execute();
        System.out.println("result="+result);
    }

    @Test
    public void testSearch() throws Exception {
        ActionProxy proxy = super.getActionProxy("/swp/page/search.do");
        Assert.assertNotNull(proxy);
        String result = proxy.execute();
        System.out.println("result="+result);
    }

    @Test
    public void testCreate() throws Exception {
        this.request.addHeader("X-Requested-With", "XMLHttpRequest");
        this.request.addParameter("ids", "18");
        ActionProxy proxy = super.getActionProxy("/swp/page/create.do");
        Assert.assertNotNull(proxy);
        String result = proxy.execute();
        System.out.println("result="+result);
    }
}
