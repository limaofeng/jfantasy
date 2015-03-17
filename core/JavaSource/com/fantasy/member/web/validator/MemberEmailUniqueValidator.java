package com.fantasy.member.web.validator;

import org.springframework.beans.factory.annotation.Autowired;

import org.hibernate.criterion.Restrictions;

import com.fantasy.framework.struts2.validator.validators.AjaxValidatorSupport;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.member.bean.Member;
import com.fantasy.member.service.MemberService;
import com.opensymphony.xwork2.validator.ValidationException;

/**
 * 验证用户email唯一
 * 
 * @Author lsz
 * @Date 2013-12-12 下午6:08:25
 * 
 */
public class MemberEmailUniqueValidator extends AjaxValidatorSupport {

	@Autowired
	private MemberService memberService;

	@Override
	public void validate(Object obj) throws ValidationException {
		String fieldName = getFieldName();
		String email = StringUtil.nullValue(getFieldValue(fieldName, obj));
		Member member = memberService.findUnique(Restrictions.eq("details.email", email), Restrictions.eq("details.mailValid", true));
		if (member != null) {
			addFieldError(fieldName, obj);
		}
	}

}
