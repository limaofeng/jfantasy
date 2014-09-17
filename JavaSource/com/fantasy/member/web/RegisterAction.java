package com.fantasy.member.web;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.fantasy.framework.struts2.ActionSupport;
import com.fantasy.member.bean.Member;
import com.fantasy.member.bean.MemberDetails;
import com.fantasy.member.service.MemberService;
import com.fantasy.member.userdetails.MemberUser;
import com.fantasy.security.SpringSecurityUtils;
import com.fantasy.system.service.ConfigService;

/**
 * @Author lsz
 * @Date 2013-12-10 上午9:25:46
 * 
 */
public class RegisterAction extends ActionSupport {

	private static final long serialVersionUID = -7467306241895113610L;

	@Resource
	private MemberService memberService;// 会员
	@Resource(name = "memberUserDetailsService")
	private UserDetailsService userDetailsService;
	@Resource(name = "memberSuccessHandlers")
	private List<AuthenticationSuccessHandler> successHandlers;

	/**
	 * 会员注册成功
	 * 
	 * @param member
	 * @return
	 * @throws Exception
	 */
	public String register(Member member) throws Exception {
		member = this.memberService.register(member);
		UserDetails userDetails = userDetailsService.loadUserByUsername(member.getUsername());
		SpringSecurityUtils.saveUserDetailsToContext(userDetails, (HttpServletRequest) request);
		for (AuthenticationSuccessHandler handler : successHandlers) {
			handler.onAuthenticationSuccess((HttpServletRequest) request, (HttpServletResponse) response, SecurityContextHolder.getContext().getAuthentication());
		}
//		if(member.getDetails().getEmail()!=null){
//			return "email";
//		}
		session.put("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
		return SUCCESS;
	}
	
	/**
	 * 邮箱注册成功发送邮件验证
	 * @return
	 */
	public String sucemail(){
		MemberUser memberUser = SpringSecurityUtils.getCurrentUser(MemberUser.class);
		Member member = this.memberService.get(memberUser.getUser().getId());
		String title = ConfigService.get("website", "sendeamiltitle").getName();
		String url ="/reg/emailv3.do?memberId=" + member.getId();
		String ftl="/template/reg/send_email.ftl";
		this.memberService.sendemail(member,url,title,ftl);
		return SUCCESS;
	}

	/**
	 * 邮箱验证第二步发送邮件
	 * 
	 * @param member
	 * @return
	 */
	public String emailv2(String email) {
		MemberUser memberUser = SpringSecurityUtils.getCurrentUser(MemberUser.class);
		Member member = this.memberService.get(memberUser.getUser().getId());
		member.getDetails().setEmail(email);
		String title = ConfigService.get("website", "sendeamiltitle").getName();
		String url ="itsm/reg/emailv3.do?memberId=" + member.getId();
		String ftl="/template/reg/send_email.ftl";
		this.memberService.sendemail(member,url,title,ftl);
		return SUCCESS;
	}
	
	/**
	 * 接收邮件反馈信息
	 * @param memberId
	 * @return
	 */
	public String emailv3(Long memberId){
		MemberUser memberUser = SpringSecurityUtils.getCurrentUser(MemberUser.class);
		Member member = this.memberService.get(memberUser.getUser().getId());
		if(member.getId().compareTo(memberId)==0){
			member.getDetails().setMailValid(false);
		}
		return SUCCESS;
	}
	
	/**
	 * 判断用户是否进行过邮箱验证
	 * @param username
	 * @return
	 */
	public String findpwdv2(String username){
		Member member=this.memberService.findUniqueByUsername(username);
		MemberDetails details=member.getDetails();
		if(details.getEmail()!=null&&details.getMailValid()){
			return SUCCESS;
		}else{
			return "wrong";
		}
	}

}
