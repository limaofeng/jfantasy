package com.fantasy.member.ws.server;

import com.fantasy.framework.ws.util.WebServiceUtil;
import com.fantasy.member.bean.Member;
import com.fantasy.member.service.MemberService;
import com.fantasy.member.ws.IMemberService;
import com.fantasy.member.ws.dto.MemberDTO;
import com.fantasy.security.SpringSecurityUtils;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class MemberWebService implements IMemberService {

    @Resource
    private MemberService memberService;

    @Override
    public MemberDTO register(MemberDTO member) {
        return WebServiceUtil.toBean(memberService.register(WebServiceUtil.toBean(member, Member.class)), MemberDTO.class);
    }

    @Override
    public MemberDTO findUniqueByUsername(String username) {
        return WebServiceUtil.toBean(memberService.findUniqueByUsername(username), MemberDTO.class);
    }

    @Override
    public boolean isPasswordValid(String encPass, String rawPass) {
        PasswordEncoder encoder = SpringSecurityUtils.getPasswordEncoder();
        return encoder.isPasswordValid(encPass, rawPass, null);
    }

    @Override
    public void login(String username) {
        memberService.login(memberService.findUniqueByUsername(username));
    }

    @Override
    public MemberDTO update(MemberDTO member) {
        return null;
    }


}
