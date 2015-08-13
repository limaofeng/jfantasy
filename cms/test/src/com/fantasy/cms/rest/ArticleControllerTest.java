package com.fantasy.cms.rest;

import com.fantasy.cms.bean.Article;
import com.fantasy.cms.bean.ArticleCategory;
import com.fantasy.cms.service.CmsService;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.error.IgnoreException;
import com.fantasy.framework.util.common.JdbcUtil;
import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class ArticleControllerTest {

    private final static Log LOG = LogFactory.getLog(ArticleControllerTest.class);

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Autowired
    private CmsService cmsService;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        //创建分类
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/cms/categorys").param("code","admintest").param("name","接口测试")).andDo(MockMvcResultHandlers.print()).andReturn();
        Assert.assertEquals(200, result.getResponse().getStatus());
        //创建文章
        this.testSave();
    }

    @After
    public void tearDown() throws Exception {
        ArticleCategory articleCategory = this.cmsService.get("admintest");
        if(articleCategory!=null){
            this.cmsService.delete(articleCategory.getCode());
        }
    }


    @Test
    public void testFindPager() throws Exception {
        MvcResult result = JdbcUtil.transaction(new JdbcUtil.Callback<MvcResult>() {
            @Override
            public MvcResult run() {
                try {
                    return mockMvc.perform(MockMvcRequestBuilders.get("/cms/articles?pageSize=15&EQS_category.code=admintest")).andDo(MockMvcResultHandlers.print()).andReturn();
                } catch (Exception e) {
                    throw new IgnoreException(e.getMessage(), e);
                }
            }
        }, TransactionDefinition.PROPAGATION_REQUIRED);
        Assert.assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    public void testView() throws Exception{
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        filters.add(new PropertyFilter("EQS_title", "测试文章保存"));
        List<Article> articleList = this.cmsService.findPager(new Pager<Article>(1),filters).getPageItems();
        if(!articleList.isEmpty()){
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/cms/articles/"+articleList.get(0).getId())).andDo(MockMvcResultHandlers.print()).andReturn();
            Assert.assertEquals(200, result.getResponse().getStatus());
        }

    }

    public void testSave() throws Exception{
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/cms/articles").param("title","测试文章保存").param("summary","保存").param("category.code","admintest").param("content.text","测试文章保存测试文章保存测试文章保存测试文章保存测试文章保存测试文章保存")).andDo(MockMvcResultHandlers.print()).andReturn();
        Assert.assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    public void testUpdate() throws Exception{
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        filters.add(new PropertyFilter("EQS_title","测试文章保存"));
        List<Article> articleList = this.cmsService.findPager(new Pager<Article>(1),filters).getPageItems();
        if(!articleList.isEmpty()){
            final String id = articleList.get(0).getId().toString();
            MvcResult result = JdbcUtil.transaction(new JdbcUtil.Callback<MvcResult>() {
                @Override
                public MvcResult run() {
                    try {
                        return mockMvc.perform(MockMvcRequestBuilders.put("/cms/articles/"+id).param("summary","保存222")).andDo(MockMvcResultHandlers.print()).andReturn();
                    } catch (Exception e) {
                        throw new IgnoreException(e.getMessage(), e);
                    }
                }
            }, TransactionDefinition.PROPAGATION_REQUIRED);
            Assert.assertEquals(200, result.getResponse().getStatus());
        }
    }

    @Test
    public void testDelete() throws Exception{
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        filters.add(new PropertyFilter("EQS_title","测试文章保存"));
        List<Article> articleList = this.cmsService.findPager(new Pager<Article>(1),filters).getPageItems();
        if(!articleList.isEmpty()){
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/cms/articles/"+articleList.get(0).getId())).andDo(MockMvcResultHandlers.print()).andReturn();
            Assert.assertEquals(200, result.getResponse().getStatus());
        }
    }
}
