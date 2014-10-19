package com.fantasy.member.userdetails;

import java.util.Locale;

import javax.annotation.Resource;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.member.bean.Member;
import com.fantasy.member.service.MemberService;

public class MemberUserDetailsService implements UserDetailsService {

	protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

	@Resource
	private MemberService memberService;

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Member member = memberService.findUniqueByUsername(username);
		if (ObjectUtil.isNull(member)) {
			throw new UsernameNotFoundException(messages.getMessage("JdbcDaoImpl.notFound", new Object[] { username }, "Username {0} not found", Locale.CANADA));
		}
		return new MemberUser(member);
	}

}