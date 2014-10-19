package com.fantasy.security.userdetails.checker;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;

import com.fantasy.security.userdetails.FantasyUserDetails;
import com.fantasy.security.userdetails.SimpleUser;
import com.fantasy.security.userdetails.exception.FirstLoginAuthenticationException;

/**
 * 密码过期验证
 * 
 * @author Administrator
 * 
 */
public class PasswordExpiredUserDetailsChecker implements UserDetailsChecker {

	@SuppressWarnings( { "unchecked", "unused" })
	public void check(UserDetails userDetails) {
		SimpleUser<FantasyUserDetails> simpleUser = (SimpleUser<FantasyUserDetails>) userDetails;
		// 每90天需要换新密码!
		throw new FirstLoginAuthenticationException("密码过期(密码有效期为90天)，请修改密码!");
	}

}
