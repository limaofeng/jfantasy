package com.fantasy.member.web.validator;

import com.fantasy.framework.struts2.core.validator.validators.AjaxValidatorSupport;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.member.bean.Member;
import com.fantasy.member.service.MemberService;
import com.opensymphony.xwork2.validator.ValidationException;

/**
 * @Author lsz
 * @Date 2013-12-9 上午11:42:55
 * 
 */
public class MemberExistValidator extends AjaxValidatorSupport {

	private MemberService memberService;

	public void setMemberService(MemberService memberService) {
		this.memberService = memberService;
	}

	@Override
	public void validate(Object obj) throws ValidationException {
		String fieldName = getFieldName();
		String username = StringUtil.nullValue(getFieldValue(fieldName, obj));
		Member member = memberService.findUniqueByUsername(username);
		if (member == null) {
			addFieldError(fieldName, obj);
		}
	}
}
