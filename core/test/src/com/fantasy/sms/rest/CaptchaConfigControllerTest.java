package com.fantasy.sms.rest;

import com.fantasy.framework.util.jackson.JSON;
import com.fantasy.sms.bean.CaptchaConfig;
import com.fantasy.sms.service.CaptchaConfigService;
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
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class CaptchaConfigControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Autowired
    private CaptchaConfigService captchaConfigService;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testSearch() throws Exception {

    }

    @Test
    public void testView() throws Exception {

    }

    @Test
    public void testCreate() throws Exception {
        CaptchaConfig config = new CaptchaConfig();
        config.setId("TEST");
        config.setServerName("测试短信验证码");
        config.setTemplate("验证码为:{captcha}");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/sms/configs").content(JSON.serialize(config)).contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print()).andReturn();
        Assert.assertEquals(HttpStatus.CREATED.value(), result.getResponse().getStatus());
    }

    @Test
    public void testUpdate() throws Exception {

    }

    @Test
    public void testDelete() throws Exception {

    }

    @Test
    public void testDelete1() throws Exception {

    }
}