package org.jfantasy.test.rest;

import junit.framework.Assert;
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
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class FormModelControllerTest {

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
    public void testUser() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/formmodel/user?user.username=zhang&user.password=123")).andDo(MockMvcResultHandlers.print()).andReturn();
        Assert.assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    public void testArray() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/formmodel/array1?array[0]=zhang&array[1]=li")).andDo(MockMvcResultHandlers.print()).andReturn();
        Assert.assertEquals(200, result.getResponse().getStatus());

        result = mockMvc.perform(MockMvcRequestBuilders.get("/formmodel/array2?array[0].username=zhang&array[0].password=123&array[1].username=li")).andDo(MockMvcResultHandlers.print()).andReturn();
        Assert.assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    public void testList() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/formmodel/list1?list[0]=123&list[1]=234")).andDo(MockMvcResultHandlers.print()).andReturn();
        Assert.assertEquals(200, result.getResponse().getStatus());

        result = mockMvc.perform(MockMvcRequestBuilders.get("/formmodel/list2?list[0].username=zhang&list[1].username=li")).andDo(MockMvcResultHandlers.print()).andReturn();
        Assert.assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    public void testMap() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/formmodel/map1?map['0']=123&map[\"1\"]=234")).andDo(MockMvcResultHandlers.print()).andReturn();
        Assert.assertEquals(200, result.getResponse().getStatus());

        result = mockMvc.perform(MockMvcRequestBuilders.get("/formmodel/map2?map['0'].password=123&map['0'].username=123&map[\"1\"].username=234")).andDo(MockMvcResultHandlers.print()).andReturn();
        Assert.assertEquals(200, result.getResponse().getStatus());
    }
}