package org.jfantasy.member.userdetails;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.member.bean.Member;
import org.jfantasy.member.service.MemberService;

public class MemberUserDetailsService implements UserDetailsService {

    protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

    @Autowired
    private MemberService memberService;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberService.findUniqueByUsername(username);
        if (ObjectUtil.isNull(member)) {
            throw new UsernameNotFoundException(messages.getMessage("JdbcDaoImpl.notFound", new Object[]{username}, "Username {0} not found", Locale.CANADA));
        }
        return new MemberUser(member);
    }

}