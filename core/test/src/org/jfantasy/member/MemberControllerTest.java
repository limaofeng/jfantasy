//package org.jfantasy.member;
//
//import org.jfantasy.framework.util.jackson.JSON;
//import junit.framework.Assert;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.context.web.WebAppConfiguration;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@WebAppConfiguration(value = "src/main/webapp")
//@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
//public class MemberControllerTest {
//
//    @Autowired
//    private WebApplicationContext context;
//
//    private MockMvc mockMvc;
//
//    @Autowired
//    private MemberService memberService;
//
//    @Before
//    public void setUp() throws Exception {
//        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
//        this.testSave();
//    }
//
//    @After
//    public void tearDown() throws Exception {
//        Member member = this.memberService.findUniqueByUsername("membertest");
//        if (member != null) {
//            this.memberService.delete(member.getId());
//        }
//    }
//
//    @Test
//    public void testFindPager() throws Exception {
//        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/members?pageSize=15&LIKES_username=membertest")).andDo(MockMvcResultHandlers.print()).andReturn();
//        Assert.assertEquals(200, result.getResponse().getStatus());
//    }
//
//    @Test
//    public void testView() throws Exception {
//        Member member = this.memberService.findUniqueByUsername("membertest");
//        if (member != null) {
//            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/members/" + member.getId())).andDo(MockMvcResultHandlers.print()).andReturn();
//            Assert.assertEquals(200, result.getResponse().getStatus());
//        }
//    }
//
//    public void testSave() throws Exception {
//        Map<String, Object> member = new HashMap<String, Object>();
//        member.put("username", "membertest");
//        member.put("password", "123456");
//        member.put("enabled", true);
//        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/members").content(JSON.serialize(member)).contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print()).andReturn();
//        Assert.assertEquals(HttpStatus.CREATED.value(), result.getResponse().getStatus());
//    }
//
//
//    @Test
//    public void testUpdate() throws Exception {
//        Member member = this.memberService.findUniqueByUsername("membertest");
//        if (member != null) {
//            Map<String, Object> memberMap = new HashMap<String, Object>();
//            memberMap.put("id", member.getId());
//            memberMap.put("password", "666666");
//            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/members/" + member.getId()).content(JSON.serialize(memberMap)).contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print()).andReturn();
//            Assert.assertEquals(200, result.getResponse().getStatus());
//        }
//    }
//
//    @Test
//    public void testDelete() throws Exception {
//        Member member = this.memberService.findUniqueByUsername("membertest");
//        if (member != null) {
//            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/members/" + member.getId())).andDo(MockMvcResultHandlers.print()).andReturn();
//            Assert.assertEquals(HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus());
//        }
//    }
//}
