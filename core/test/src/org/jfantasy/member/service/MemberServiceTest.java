package org.jfantasy.member.service;

import org.jfantasy.member.bean.Member;
import org.jfantasy.member.bean.MemberDetails;
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

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class MemberServiceTest {

    private final static Log LOG = LogFactory.getLog(MemberServiceTest.class);

    @Autowired
    private MemberService memberService;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {
        Member member = memberService.findUniqueByUsername("test");
        if(member != null){
            this.memberService.delete(member.getId());
        }
    }

    public void testFindPager() throws Exception {

    }

    public void testRegister() throws Exception {

    }

    public void testSendemail() throws Exception {

    }

    public void testFind() throws Exception {

    }

    public void testFindUnique() throws Exception {

    }

    @Test
    public void testSave() throws Exception {
        Member member = memberService.findUniqueByUsername("test");
        if(member == null) {
            member =  new Member();
            member.setUsername("test");
            member.setPassword("123456");
            member.setNickName("测试账号");
            memberService.save(member);
        }

        member = memberService.findUniqueByUsername("test");
        Assert.assertNotNull(member);

        Member updateMember =  new Member();
        updateMember.setId(member.getId());
        updateMember.setNickName("测试");
        updateMember.setDetails(new MemberDetails());
        updateMember.getDetails().setName("测试");

        updateMember = memberService.save(updateMember);

        LOG.debug(updateMember);

    }

    public void testGet() throws Exception {

    }

    public void testDelete() throws Exception {

    }

    public void testLogin() throws Exception {

    }

    public void testLogout() throws Exception {

    }

    public void testFindUniqueByUsername() throws Exception {

    }
}