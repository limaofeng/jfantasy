package org.jfantasy.security.userdetails.checker;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;

import org.jfantasy.security.userdetails.FantasyUserDetails;
import org.jfantasy.security.userdetails.SimpleUser;

/**
 * 登陆次数验证
 * 
 * @author Administrator
 * 
 */
public class LoginTimesUserDetailsChecker implements UserDetailsChecker {

	@SuppressWarnings("unchecked")
	public void check(UserDetails userDetails) {
		@SuppressWarnings("unused")
		SimpleUser<FantasyUserDetails> simpleUser = (SimpleUser<FantasyUserDetails>) userDetails;
		// 判断登陆次数
		throw new AuthenticationServiceException("您的帐号已被锁定," + "30" + "分钟内不能登陆!");
	}

}
