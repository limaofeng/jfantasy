package com.fantasy.member.web;

import javax.annotation.Resource;

import com.fantasy.framework.struts2.ActionSupport;
import com.fantasy.member.bean.Member;
import com.fantasy.member.service.MemberService;
import com.fantasy.system.service.ConfigService;

/**
 * @Author lsz
 * @Date 2013-12-10 上午9:25:46
 * 
 */
public class FindPwdAction extends ActionSupport {

	private static final long serialVersionUID = -7467306241895113610L;

	@Resource
	private MemberService memberService;// 会员

	/**
	 * 判断用户是否进行过邮箱验证
	 * 
	 * @param username
	 * @return
	 */
	public String findpwdv2(String username) {
		Member member = this.memberService.findUniqueByUsername(username);
		if (!member.getDetails().getMailValid() && !member.getDetails().getMobileValid()) {
			return "wrong";
		}
		if (member.getDetails().getMailValid()) {
			this.attrs.put("findType", "mail");
		} else if (member.getDetails().getMobileValid()) {
			this.attrs.put("findType", "mobile");
		}
		this.attrs.put("member", member);
		return SUCCESS;
	}

	/**
	 * 找回密码发送邮件
	 * 
	 * @param username
	 * @return
	 */
	public String findpwdsend(String username) {
		Member member = this.memberService.findUniqueByUsername(username);
		String title = ConfigService.get("website", "findpwd").getName();
		String url = "/findpwd/findpwdv3.do?username=" + member.getUsername();
		String ftl = "/template/reg/send_email.ftl";
		this.memberService.sendemail(member, url, title, ftl);
		this.attrs.put("email", member.getDetails().getEmail());
		return SUCCESS;
	}

	/**
	 * 重新输入密码
	 * 
	 * @param username
	 * @return
	 */
	public String findpwdv3(String username) {
		this.attrs.put("username", username);
		return SUCCESS;
	}

	/**
	 * 密码修改成功
	 * 
	 * @param username
	 * @return
	 */
	public String findpwdv4(Member member) {
		Member memberold = this.memberService.findUniqueByUsername(member.getUsername());
		memberold.setPassword(member.getPassword());
		return SUCCESS;
	}

}
