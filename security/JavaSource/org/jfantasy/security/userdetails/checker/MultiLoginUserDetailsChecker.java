package org.jfantasy.security.userdetails.checker;

import org.jfantasy.security.userdetails.FantasyUserDetails;
import org.jfantasy.security.userdetails.SimpleUser;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;

/**
 * 重复登陆验证
 * 
 * @author Administrator
 * 
 */
public class MultiLoginUserDetailsChecker implements UserDetailsChecker {

	@SuppressWarnings( { "unchecked", "unused" })
	public void check(UserDetails userDetails) {
		SimpleUser<FantasyUserDetails> simpleUser = (SimpleUser<FantasyUserDetails>) userDetails;
		// 验证20分钟之内 不能同时登陆多个用户
		throw new AuthenticationServiceException("您的帐号已经登录或者您前一次登录后没有按“退出系统”按钮正常退出，<br/>请您在等待20分钟(左右)后重新登录，如不能登录请联系相关部门删除前一次登录记录!");
	}

}
