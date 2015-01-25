package com.fantasy.swp;

import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.swp.bean.Template;
import com.fantasy.swp.service.TemplateService;
import com.opensymphony.xwork2.ActionProxy;
import org.apache.struts2.StrutsSpringJUnit4TestCase;
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

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wml on 2015/1/23.
 */
@Component
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class TemplateActionTest extends StrutsSpringJUnit4TestCase {

    @Resource
    private TemplateService templateService;

    @Override
    protected String getConfigPath() {
        return "struts.xml";
    }

    @Before
    public void setUp() throws Exception {
        JspSupportServlet jspSupportServlet = new JspSupportServlet();
        jspSupportServlet.init(new MockServletConfig());
        super.setUp();
        //request.getSession().setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testSave() throws Exception {
        this.request.addHeader("X-Requested-With", "XMLHttpRequest");
        this.request.addParameter("name", "ARTICLE_JUNIT_TEST");
        this.request.addParameter("description", "新闻文章");
        this.request.addParameter("webSite.id", "7");
//        this.request.addParameter("content", template.getContent());
        this.request.addParameter("path", "/template/template_test.ftl");
        this.request.addParameter("dataInferfaces[0].name", "文章标题");
        this.request.addParameter("dataInferfaces[0].key", "article.title");
        this.request.addParameter("dataInferfaces[1].name", "文章摘要");
        this.request.addParameter("dataInferfaces[1].key", "article.summary");
        this.request.setContent(TemplateActionTest.getFileContent(TemplateActionTest.class.getClass().getResource("/").getPath()+"template/template_test.ftl").getBytes());
        ActionProxy proxy = super.getActionProxy("/swp/template/save.do");
        String result = proxy.execute();
        System.out.println("result=" + result);
    }

    @Test
    public void testDelete() throws Exception {
        this.request.removeAllParameters();
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        filters.add(new PropertyFilter("EQS_name","ARTICLE_JUNIT_TEST"));
        List<Template> templates = this.templateService.find(filters);
        if(templates==null || templates.size()<=0){
            this.testSave();
            templates = this.templateService.find(filters);
        }
        Assert.assertNotNull(templates);
        for(int i=0; i<templates.size(); i++){
            this.request.addParameter("ids", templates.get(i).getId()+"");
        }
        ActionProxy proxy = super.getActionProxy("/swp/template/delete.do");
        Assert.assertNotNull(proxy);

        String result = proxy.execute();
        System.out.println("result="+result);
    }

    @Test
    public void testSearch() throws Exception {
        ActionProxy proxy = super.getActionProxy("/swp/template/search.do");
        Assert.assertNotNull(proxy);
        String result = proxy.execute();
        System.out.println("result="+result);
    }


    private static String getFileContent(String filePath){
        StringBuilder fileSb = new StringBuilder("");
        try {
            File file = new File(filePath);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String tmpStr = "";
            while ((tmpStr = bufferedReader.readLine()) != null) {
                fileSb.append(tmpStr);
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileSb.toString();
    }

}
