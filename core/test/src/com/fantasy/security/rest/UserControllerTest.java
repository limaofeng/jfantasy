package com.fantasy.security.rest;

import com.fantasy.framework.error.IgnoreException;
import com.fantasy.framework.util.common.JdbcUtil;
import com.fantasy.framework.util.jackson.JSON;
import com.fantasy.security.bean.Role;
import com.fantasy.security.bean.User;
import com.fantasy.security.service.UserService;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

import java.util.ArrayList;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class UserControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        this.testSave();
    }

    @After
    public void tearDown() throws Exception {
        User user = this.userService.findUniqueByUsername("usertest");
        if(user!=null){
            this.userService.delete(user.getId());
        }
    }

    @Test
    public void testFindPager() throws Exception {
        MvcResult result = JdbcUtil.transaction(new JdbcUtil.Callback<MvcResult>() {
            @Override
            public MvcResult run() {
                try {
                    return mockMvc.perform(MockMvcRequestBuilders.get("/users?pageSize=15&LIKES_username=usertest")).andDo(MockMvcResultHandlers.print()).andReturn();
                } catch (Exception e) {
                    throw new IgnoreException(e.getMessage(), e);
                }
            }
        }, TransactionDefinition.PROPAGATION_REQUIRED);
        Assert.assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    public void testView() throws Exception{
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/users/usertest")).andDo(MockMvcResultHandlers.print()).andReturn();
        Assert.assertEquals(200, result.getResponse().getStatus());
    }

    public void testSave() throws Exception{
        User user = new User();
        user.setUsername("usertest");
        user.setPassword("123456");
        user.setEnabled(true);
        user.setRoles(new ArrayList<Role>());
        Role role = new Role();
        role.setCode("SYSTEM");
        user.getRoles().add(role);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/users").content(JSON.serialize(user)).contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print()).andReturn();
        Assert.assertEquals(HttpStatus.CREATED.value(), result.getResponse().getStatus());
    }


    @Test
    public void testUpdate() throws Exception{
        final User user = this.userService.findUniqueByUsername("usertest");
        if(user!=null){
            user.setPassword("666666");
            MvcResult result = JdbcUtil.transaction(new JdbcUtil.Callback<MvcResult>() {
                @Override
                public MvcResult run() {
                    try {
                        return mockMvc.perform(MockMvcRequestBuilders.put("/users/"+user.getId()).content(JSON.serialize(user)).contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print()).andReturn();
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
        User user = this.userService.findUniqueByUsername("usertest");
        if(user!=null){
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/users/"+user.getId())).andDo(MockMvcResultHandlers.print()).andReturn();
            Assert.assertEquals(200, result.getResponse().getStatus());
        }
    }
}
