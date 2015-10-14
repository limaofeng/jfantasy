package com.fantasy.security.rest;

import com.fantasy.framework.error.IgnoreException;
import com.fantasy.framework.util.common.JdbcUtil;
import com.fantasy.framework.util.jackson.JSON;
import com.fantasy.security.bean.Resource;
import com.fantasy.security.bean.enums.ResourceType;
import com.fantasy.security.service.ResourceService;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class ResourceControllerTest {

    @Autowired
    private WebApplicationContext context;
    @Autowired
    private ResourceService resourceService;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        this.testCreate();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testSearch() throws Exception {
        MvcResult result = JdbcUtil.transaction(new JdbcUtil.Callback<MvcResult>() {
            @Override
            public MvcResult run() {
                try {
                    return mockMvc.perform(MockMvcRequestBuilders.get("/security/resources?pageSize=15")).andDo(MockMvcResultHandlers.print()).andReturn();
                } catch (Exception e) {
                    throw new IgnoreException(e.getMessage(), e);
                }
            }
        }, TransactionDefinition.PROPAGATION_REQUIRED);
        Assert.assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    public void testDelete() throws Exception {

    }

    public void testCreate() throws Exception {
        Resource resource = new Resource();
        resource.setName("测试");
        resource.setType(ResourceType.url);
        resource.setValue("/test/**");
        resource.setEnabled(true);
        resource.setDescription("权限管理");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/security/resources").content(JSON.serialize(resource)).contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print()).andReturn();
        Assert.assertEquals(201, result.getResponse().getStatus());
    }

    @Test
    public void testUpdate() throws Exception {
//        Resource resource = new Resource();
//        resource.setName("测试");
//        resource.setType(ResourceType.url);
//        resource.setValue("/test/**");
//        resource.setEnabled(true);
//        resource.setDescription("权限管理");
//
//        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/security/resources").content(JSON.serialize(resource)).contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print()).andReturn();
//        Assert.assertEquals(201, result.getResponse().getStatus());
    }
}