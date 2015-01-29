package com.fantasy.swp;

import com.fantasy.attr.bean.Article;
import com.fantasy.attr.service.ArticleService;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.util.jackson.JSON;
import com.fantasy.swp.bean.Data;
import com.fantasy.swp.bean.DataInferface;
import com.fantasy.swp.service.DataInferfaceService;
import com.fantasy.swp.service.DataService;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wml on 2015/1/25.
 */
@Component
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class DataActionTest extends StrutsSpringJUnit4TestCase {

    @Resource
    private DataInferfaceService dataInferfaceService;
    @Resource
    private DataService dataService;
    @Resource
    private TemplateActionTest templateActionTest;
    @Resource
    private ArticleService articleService;

    @Before
    public void setUp() throws Exception {
        JspSupportServlet jspSupportServlet = new JspSupportServlet();
        jspSupportServlet.init(new MockServletConfig());
        super.setUp();
        templateActionTest.setUp();
    }

    @After
    public void tearDown() throws Exception {
        templateActionTest.testDelete();
        templateActionTest.tearDown();
        testDelete();
    }

//    @Test
    public void testSave() throws Exception {
        this.request.addHeader("X-Requested-With", "XMLHttpRequest");
        this.request.addParameter("description", "DATA_JUNIT_TEST");

        this.request.addParameter("scope", "page");
        this.request.addParameter("cacheInterval", "0");
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        filters.add(new PropertyFilter("EQS_template.name","TEMPLATE_JUNIT_TEST"));
        List<DataInferface> dataInferfaces = this.dataInferfaceService.find(filters);
        if(dataInferfaces==null || dataInferfaces.size()<=0){
            templateActionTest.testSave();
            dataInferfaces = this.dataInferfaceService.find(filters);
        }
        Assert.assertNotNull(dataInferfaces);
        for(int i=0; i<dataInferfaces.size(); i++){
            this.request.removeParameter("dataInferface.id");
            this.request.removeParameter("value");

            this.request.addParameter("dataInferface.id", dataInferfaces.get(i).getId()+"");
            if(dataInferfaces.get(i).getDataSource()==DataInferface.DataSource.stat){// 静态
                if(dataInferfaces.get(i).getDataType()==DataInferface.DataType.list){ // 数组类型
                    filters = new ArrayList<PropertyFilter>();
                    List<Article> articles = this.articleService.find(filters);
                    this.request.addParameter("value", JSON.serialize(articles));
                }else if(dataInferfaces.get(i).getDataType()==DataInferface.DataType.common){  // 普通类型
                    this.request.addParameter("value", dataInferfaces.get(i).getKey()+"XXXXX");
                }
            }else if(dataInferfaces.get(i).getDataSource()==DataInferface.DataSource.func){
                if(dataInferfaces.get(i).getDataType()==DataInferface.DataType.list){ // 数组类型
//                    filters = new ArrayList<PropertyFilter>();
//                    List<Article> articles = this.articleService.find(filters);
                    // "{'func':'#articleService.find(#filters)','params':{filters:[{key:'EQS_creator',value:'hebo'}]}}"
                    this.request.addParameter("value", "{'func':'#articleService.findUniqueBy(#propertyName,#value)','params':{filters:[{key:'EQS_creator',value:'hebo'}],stat:{propertyName:'title',value:'titleA2'} }}");
                }else if(dataInferfaces.get(i).getDataType()==DataInferface.DataType.common){  // 普通类型
                    this.request.addParameter("value", "{'func':'#articleService.findUniqueBy(#propertyName,#value)','params':{filters:[{key:'EQS_creator',value:'hebo'}],stat:{propertyName:'title',value:'titleA2'} }}");
                }
            }


            ActionProxy proxy = super.getActionProxy("/swp/page/data-save.do");
            String result = proxy.execute();
            System.out.println("result=" + result);
        }
    }

//    @Test
    public void testDelete() throws Exception {
        this.request.removeAllParameters();
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        filters.add(new PropertyFilter("EQS_description","DATA_JUNIT_TEST"));
        List<Data> datas = this.dataService.find(filters);
        if(datas==null || datas.size()<=0){
//            this.testSave();
//            datas = this.dataService.find(filters);
            return;
        }
        Assert.assertNotNull(datas);
        for(int i=0; i<datas.size(); i++){
            this.request.addParameter("ids", datas.get(i).getId()+"");
        }
        ActionProxy proxy = super.getActionProxy("/swp/page/data-delete.do");
        Assert.assertNotNull(proxy);

        String result = proxy.execute();
        System.out.println("result="+result);
    }

//    @Test
    public void testSearch() throws Exception {
        ActionProxy proxy = super.getActionProxy("/swp/page/data-search.do");
        Assert.assertNotNull(proxy);
        String result = proxy.execute();
        System.out.println("result="+result);
    }
}
