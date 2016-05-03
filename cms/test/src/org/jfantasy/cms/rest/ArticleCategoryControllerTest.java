package org.jfantasy.cms.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.jfantasy.cms.ApplicationTest;
import org.jfantasy.cms.bean.Article;
import org.jfantasy.cms.bean.ArticleCategory;
import org.jfantasy.cms.service.CmsService;
import org.jfantasy.test.AbstractClientTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(ApplicationTest.class)
public class ArticleCategoryControllerTest extends AbstractClientTest {

    private MockRestServiceServer mockServer;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        //模拟一个服务器
        mockServer = createServer();

//        this.testSave();
    }

    @Autowired
    private CmsService cmsService;

    @After
    public void tearDown() throws Exception {
        ArticleCategory articleCategory = this.cmsService.get("admintest");
        if(articleCategory!=null){
            this.cmsService.delete(articleCategory.getCode());
        }
    }

    @Test
    public void testFindById() throws JsonProcessingException {

    }


    @Test
    @Transactional
    public void testFindPager() throws Exception {
        String uri = baseUri + "/cms/categorys?pageSize=30&EQS_code=admintest";
        String requestUri = UriComponentsBuilder.fromUriString(uri).toUriString();

        //添加服务器端断言
        mockServer.expect(requestTo(requestUri)).andExpect(method(HttpMethod.GET)).andRespond(withSuccess("", MediaType.APPLICATION_JSON));

        //2、访问URI（与API交互）
        ResponseEntity<Article> entity = restTemplate.getForEntity(uri, Article.class);

        //3.1、客户端验证
        assertEquals(HttpStatus.OK, entity.getStatusCode());

        //3.2、服务器端验证（验证之前添加的服务器端断言）
        mockServer.verify();

    }

    /*
    @Test
    @Transactional
    public void testView() throws Exception{
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/cms/categorys/admintest")).andDo(MockMvcResultHandlers.print()).andReturn();
        Assert.assertEquals(200, result.getResponse().getStatus());
    }

    public void testSave() throws Exception{
        ArticleCategory category = new ArticleCategory();
        category.setCode("admintest");
        category.setName("接口测试");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/cms/categorys").contentType(MediaType.APPLICATION_JSON).content(JSON.serialize(category))).andDo(MockMvcResultHandlers.print()).andReturn();
        Assert.assertEquals(HttpStatus.CREATED.value(), result.getResponse().getStatus());
    }


    @Test
    public void testUpdate() throws Exception{
        ArticleCategory category = this.cmsService.get("admintest");
        if(category!=null){
            category.setName("接口测试更新");
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/cms/categorys/" + category.getCode()).contentType(MediaType.APPLICATION_JSON).content(JSON.serialize(category))).andDo(MockMvcResultHandlers.print()).andReturn();
            Assert.assertEquals(200, result.getResponse().getStatus());
        }
    }

    @Test
    public void testDelete() throws Exception{
        ArticleCategory articleCategory = this.cmsService.get("admintest");
        if(articleCategory!=null){
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/cms/categorys/"+articleCategory.getCode())).andDo(MockMvcResultHandlers.print()).andReturn();
            Assert.assertEquals(200, result.getResponse().getStatus());
        }
    }*/


}
