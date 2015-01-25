package com.fantasy.swp;

import com.fantasy.framework.struts2.context.ActionConstants;
import com.fantasy.swp.bean.DataInferface;
import com.fantasy.swp.bean.Template;
import com.fantasy.swp.service.DataInferfaceService;
import com.fantasy.swp.service.TemplateService;
import com.fantasy.system.bean.Website;
import com.opensymphony.xwork2.ActionProxy;
import org.apache.struts2.StrutsSpringJUnit4TestCase;
import org.apache.struts2.views.JspSupportServlet;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuzhiyong on 2015/1/23.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class SwpTest2 extends StrutsSpringJUnit4TestCase {

    @Resource
    private DataInferfaceService dataInferfaceService;
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

//    @Test
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
        this.request.setContent(SwpTest2.getFileContent(SwpTest2.class.getClass().getResource("/").getPath()+"template/template_test.ftl").getBytes());
        ActionProxy proxy = super.getActionProxy("/swp/template/save.do");
        String result = proxy.execute();
        System.out.println("result="+result);
    }

    @Test
    public void testSearch2() throws Exception {
        ActionProxy proxy = super.getActionProxy("/swp/template/search.do");
        Assert.assertNotNull(proxy);

        String result = proxy.execute();

        Assert.assertEquals(ActionConstants.JSONDATA, result);
    }

    @Test
    public void testDelete() throws Exception {
        this.testSave();
        this.request.removeAllParameters();
        this.request.addParameter("ids", 2+"");
        ActionProxy proxy = super.getActionProxy("/swp/template/delete.do");
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

    @Test
    public void testSearch() throws Exception {

        ActionProxy proxy = super.getActionProxy("/swp/template/save.do");
        Assert.assertNotNull(proxy);

        String result = proxy.execute();

        Assert.assertEquals(ActionConstants.JSONDATA, result);
    }
}
