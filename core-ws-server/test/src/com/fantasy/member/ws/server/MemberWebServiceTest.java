package com.fantasy.member.ws.server;

import com.fantasy.framework.util.common.ImageUtil;
import com.fantasy.member.bean.Member;
import com.fantasy.member.service.MemberService;
import com.fantasy.member.ws.IMemberService;
import com.fantasy.member.ws.dto.MemberDTO;
import com.fantasy.member.ws.dto.MemberDetailsDTO;
import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * 会员junit测试
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class MemberWebServiceTest {

    private final static Log logger = LogFactory.getLog(MemberWebServiceTest.class);

    @Resource
    private IMemberService iMemberService;

    @Resource
    private MemberService memberService;

    @org.junit.Before
    public void setUp() throws Exception {
        this.tearDown();
        this.testRegister();
    }

    @org.junit.After
    public void tearDown() throws Exception {
        String userName = "hebo";
        Member member = this.memberService.findUniqueByUsername(userName);
        logger.debug(member);
        if(member != null) {
            this.memberService.delete(member.getId());
        }
    }

    public void testRegister() throws Exception {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setUsername("hebo");
        memberDTO.setPassword("123456");
        MemberDetailsDTO memberDetailsDTO = new MemberDetailsDTO();
        memberDetailsDTO.setName("何博");
        memberDTO.setDetails(memberDetailsDTO);
        memberDTO = this.iMemberService.register(memberDTO);
        logger.debug(memberDTO);
        assertNotNull(memberDTO.getId());
    }

    @org.junit.Test
    public void testFindUniqueByUsername() throws Exception {
        MemberDTO member = this.iMemberService.findUniqueByUsername("hebo");

        logger.debug(member);

        assertNotNull(member);
    }

    @org.junit.Test
    public void testIsPasswordValid() throws Exception {
        String userName = "hebo";
        MemberDTO member = this.iMemberService.findUniqueByUsername(userName);

        logger.debug(member);

        assertTrue(this.iMemberService.isPasswordValid("123456", member.getPassword()));

        assertFalse(this.iMemberService.isPasswordValid("23456", member.getPassword()));
    }

    @org.junit.Test
    public void testLogin() throws Exception {
        String userName = "hebo";
        this.iMemberService.login(userName);
    }

    @org.junit.Test
    public void testUpdate() throws Exception {
        String userName = "hebo";
        MemberDTO memberDTO = this.iMemberService.findUniqueByUsername(userName);
        memberDTO.setNickName("木头人123");
        MemberDetailsDTO detailsDTO = memberDTO.getDetails();
        detailsDTO.setName("何博");
        detailsDTO.setBirthday(new Date());
        detailsDTO.setEmail("393469668@qq.com");
        detailsDTO.setDescription("何博是个大笨蛋");



        //图片进行base64位编码
        detailsDTO.setAvatar(ImageUtil.getImage(MemberWebServiceTest.class.getResourceAsStream("avatar.png")));
        logger.debug(memberDTO);
        this.iMemberService.update(memberDTO);

        Assert.assertNotNull(memberDTO.getDetails().getAvatar());

        logger.debug(memberDTO);
    }
}