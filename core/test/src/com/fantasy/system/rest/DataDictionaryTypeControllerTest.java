package com.fantasy.system.rest;

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

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class DataDictionaryTypeControllerTest {

    private final static Log LOG = LogFactory.getLog(DataDictionaryTypeControllerTest.class);

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testDelete() throws Exception {

    }

    @Test
    @Transactional
    public void testDds() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/ddts?pageSize=20&EQS_code=123")).andDo(MockMvcResultHandlers.print()).andReturn();
        Assert.assertEquals(200,result.getResponse().getStatus());
    }

    @Test
    public void testSave() throws Exception {
    }

    @Test
    public void testView() throws Exception {

    }

    @Test
    public void testSearch() throws Exception {

    }
}