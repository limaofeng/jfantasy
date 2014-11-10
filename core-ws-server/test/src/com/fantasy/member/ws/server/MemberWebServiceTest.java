package com.fantasy.member.ws.server;

import com.fantasy.member.bean.Member;
import com.fantasy.member.service.MemberService;
import com.fantasy.member.ws.IMemberService;
import com.fantasy.member.ws.dto.MemberDTO;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * 会员junit测试
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class MemberWebServiceTest {

    @Resource
    private IMemberService iMemberService;

    @Resource
    private MemberService memberService;

    @org.junit.Before
    public void setUp() throws Exception {
        this.testRegister();
    }

    @org.junit.After
    public void tearDown() throws Exception {
        String userName="hebo";
        Member member =  this.memberService.findUniqueByUsername(userName);
        Long[] ids= new Long[1];
        ids[0]=member.getId();
        this.memberService.delete(ids);
    }

    public void testRegister() throws Exception {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setUsername("hebo");
        memberDTO.setPassword("123456");
        memberDTO = this.iMemberService.register(memberDTO);
        System.out.println(memberDTO.getId());
    }

    @org.junit.Test
    public void testFindUniqueByUsername() throws Exception {
        String userName="hebo";
        MemberDTO member = this.iMemberService.findUniqueByUsername(userName);
        System.out.println(member.getUsername());

    }

    @org.junit.Test
    public void testIsPasswordValid() throws Exception {
        String userName="hebo";
        MemberDTO member = this.iMemberService.findUniqueByUsername(userName);
        System.out.println(this.iMemberService.isPasswordValid("123",member.getPassword()));
    }

    @org.junit.Test
    public void testLogin() throws Exception {
        String userName="hebo";
        this.iMemberService.login(userName);
    }

    @org.junit.Test
    public void testUpdate() throws Exception {
        String userName="hebo";
        MemberDTO member = this.iMemberService.findUniqueByUsername(userName);
        member.setPassword("124");
        this.iMemberService.update(member);

    }
}