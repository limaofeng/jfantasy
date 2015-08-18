package com.fantasy.website;

import com.fantasy.file.bean.FileManagerConfig;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.website.bean.Template;
import com.fantasy.website.service.GeneratePageService;
import com.fantasy.website.service.TemplateService;
import com.fantasy.system.bean.Website;
import com.fantasy.system.service.WebsiteService;
import com.opensymphony.xwork2.ActionProxy;
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
@ContextConfiguration(locations = {"spring/applicationContext.xml"})
public class TemplateActionTest extends StrutsSpringJUnit4TestCase {

    @Autowired
    private TemplateService templateService;
    @Autowired
    private WebsiteService websiteService;
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
//        this.testDelete();
//        this.deleteWebsiteTest();
    }

//    @Test
    public void testSave() throws Exception {
        String dataType = "list";
//        // 静态:stat，方法:func，hql语句:db
//        String dataSource = "db";
        String templateFile = "template/template_test_common.ftl";
        String path = "/template/article_test_sigle.ftl";
        String key="article";
        // single,multi,pagination
        String pageType = "single";

        this.request.addHeader("X-Requested-With", "XMLHttpRequest");
        this.request.addParameter("name", "TEMPLATE_JUNIT_TEST");
        this.request.addParameter("description", "新闻文章");

        this.request.addParameter("pageType", pageType);
        if("pagination".equals(pageType)){
            // 分页
            this.request.addParameter("dataKey", key);
        }else if("multi".equals(pageType)){
            // 多页
            this.request.addParameter("dataKey", key);
        }else if("single".equals(pageType)){

        }
        //测试站点
        Website website = this.websiteService.get("SWP_WEBSITE_TEST");
        if(website==null){
            website = this.saveWebsiteTest();
        }
        this.request.addParameter("webSite.id", website.getId()+"");
//        this.request.addParameter("content", template.getContent());
        this.request.addParameter("path", path);
        this.request.addParameter("dataInferfaces[0].name", "文章列表");
        this.request.addParameter("dataInferfaces[0].key", key);
        // 数据类型
        this.request.addParameter("dataInferfaces[0].dataType", dataType);
        this.request.addParameter("dataInferfaces[1].name", "某文章");
        this.request.addParameter("dataInferfaces[1].key", "title");
        // 数据类型
        this.request.addParameter("dataInferfaces[1].dataType", "common");
        // 数据源
//        this.request.addParameter("dataInferfaces[0].dataSource", dataSource);

//        if("common".equals(dataType)){
//            this.request.addParameter("dataInferfaces[1].name", "摘要");
//            this.request.addParameter("dataInferfaces[1].key", "summary");
//            // 数据类型
//            this.request.addParameter("dataInferfaces[1].dataType", dataType);
//            // 数据源
//            if("db".equals(dataSource)){
//                this.request.addParameter("dataInferfaces[1].dataSource", "stat");
//            }else{
//                this.request.addParameter("dataInferfaces[1].dataSource", dataSource);
//            }
//        }else if("list".equals(dataType)){
//            this.request.addParameter("dataInferfaces[1].name", "上一页");
//            this.request.addParameter("dataInferfaces[1].key", "prePager");
//            // 数据类型
//            this.request.addParameter("dataInferfaces[1].dataType", "common");
//            // 数据源
//            this.request.addParameter("dataInferfaces[1].dataSource", "stat");
//            this.request.addParameter("dataInferfaces[1].name", "下一页");
//            this.request.addParameter("dataInferfaces[1].key", "nextPager");
//            // 数据类型
//            this.request.addParameter("dataInferfaces[1].dataType", "common");
//            // 数据源
//            this.request.addParameter("dataInferfaces[1].dataSource", "stat");
//        }

        this.request.setContent(TemplateActionTest.getFileContent(TemplateActionTest.class.getClass().getResource("/").getPath()+ templateFile).getBytes());
        ActionProxy proxy = super.getActionProxy("/swp/template/save.do");
        String result = proxy.execute();
        System.out.println("result=" + result);
    }

    @Test
    public void testDelete() throws Exception {
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        filters.add(new PropertyFilter("EQS_name","TEMPLATE_JUNIT_TEST"));
        List<Template> templates = this.templateService.find(filters);
        if(templates==null || templates.size()<=0){
//            this.testSave();
//            templates = this.templateService.find(filters);
            return;
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

//    @Test
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
    private Website saveWebsiteTest(){
        Website website = new Website();
        website.setKey("SWP_WEBSITE_TEST");
        website.setName("swp测试");
        website.setWeb("http://haolue.jfantasy.org");
        FileManagerConfig fileManagerConfig = new FileManagerConfig();
        fileManagerConfig.setId("haolue-default");
        website.setDefaultFileManager(fileManagerConfig);
        FileManagerConfig uploadFileManager = new FileManagerConfig();
        uploadFileManager.setId("haolue-upload");
        website.setDefaultUploadFileManager(uploadFileManager);
        return websiteService.save(website);

    }
    private void deleteWebsiteTest(){
        Website website = this.websiteService.get("SWP_WEBSITE_TEST");
        if(website!=null){
            this.websiteService.delete(website.getId());
        }
    }

//    @Test
    public void testRe() throws Exception {
        GeneratePageService reGeneratePage = SpringContextUtil.getBeanByType(GeneratePageService.class);
        reGeneratePage.reGenerate(17L);
    }
}
