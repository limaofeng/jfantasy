package com.fantasy.member.ws.server;

import com.fantasy.common.service.AreaService;
import com.fantasy.framework.util.jackson.JSON;
import com.fantasy.framework.ws.util.WebServiceUtil;
import com.fantasy.member.bean.Member;
import com.fantasy.member.bean.MemberDetails;
import com.fantasy.member.service.MemberService;
import com.fantasy.member.ws.IMemberService;
import com.fantasy.member.ws.dto.MemberDTO;
import com.fantasy.member.ws.dto.MemberDetailsDTO;
import com.fantasy.security.SpringSecurityUtils;
import com.fantasy.security.bean.enums.Sex;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class MemberWebService implements IMemberService {

    @Resource
    private MemberService memberService;

    @Resource
    private AreaService areaService;//地区信息

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
    public MemberDTO update(MemberDTO memberDTO) {
        MemberDetailsDTO detailsDTO = memberDTO.getDetails();
        Member member = this.memberService.findUniqueByUsername(memberDTO.getUsername());
        //昵称
        member.setNickName(memberDTO.getNickName());
        //会员详细
        MemberDetails details = member.getDetails();
        //姓名
        details.setName(detailsDTO.getName());
        //性别
        if(Sex.female.toString().equals(detailsDTO.getSex())||"女".equals(detailsDTO.getSex())){
            details.setSex(Sex.female);
        }else{
            details.setSex(Sex.male);
        }
        //生日
        details.setBirthday(detailsDTO.getBirthday());
        //移动电话
        details.setMobile(detailsDTO.getMobile());
        //固定电话
        details.setTel(detailsDTO.getTel());
        //邮箱
        details.setEmail(detailsDTO.getEmail());
        //描述信息
        details.setDescription(detailsDTO.getDescription());
        this.memberService.save(member);
        return memberDTO;
    }


}
