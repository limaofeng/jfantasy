package com.fantasy.cms.rest;

import com.fantasy.cms.bean.Article;
import com.fantasy.cms.service.CmsService;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
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
import org.springframework.transaction.annotation.Transactional;
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
    }

    @After
    public void tearDown() throws Exception {

    }


    @Test
    @Transactional
    public void testFindPager() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/cms/articles?pageSize=15&EQS_category.code=abcd3")).andDo(MockMvcResultHandlers.print()).andReturn();
        Assert.assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    @Transactional
    public void testView() throws Exception{
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/cms/articles/17")).andDo(MockMvcResultHandlers.print()).andReturn();
        Assert.assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    public void testSave() throws Exception{
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/cms/articles").param("title","测试文章保存").param("summary","保存").param("category.code","abcd3").param("content.text","测试文章保存测试文章保存测试文章保存测试文章保存测试文章保存测试文章保存")).andDo(MockMvcResultHandlers.print()).andReturn();
        Assert.assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    public void testUpdate() throws Exception{
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        filters.add(new PropertyFilter("EQS_title","测试文章保存"));
        List<Article> articleList = this.cmsService.findPager(new Pager<Article>(1),filters).getPageItems();
        if(!articleList.isEmpty()){
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/cms/articles/"+articleList.get(0).getId().toString()).param("summary","保存222")).andDo(MockMvcResultHandlers.print()).andReturn();
            Assert.assertEquals(200, result.getResponse().getStatus());
        }
    }


    @Test
    public void testbatchDelete() throws Exception{
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        filters.add(new PropertyFilter("EQS_title","测试文章保存"));
        List<Article> articleList = this.cmsService.findPager(new Pager<Article>(2),filters).getPageItems();
        if(!articleList.isEmpty() && articleList.size()==2){
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/cms/articles").param("id",articleList.get(0).getId().toString()).param("id",articleList.get(1).getId().toString())).andDo(MockMvcResultHandlers.print()).andReturn();
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
