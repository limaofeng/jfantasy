package com.fantasy.security.userdetails;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;

import com.fantasy.framework.util.common.ObjectUtil;

/**
 * spring security 登陆后置验证
 * 
 * @author 李茂峰
 * 
 */
public class PostAuthenticationChecks implements UserDetailsChecker, InitializingBean {

	protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

	protected final static Log LOGGER = LogFactory.getLog(PostAuthenticationChecks.class);

	private List<UserDetailsChecker> userDetailsCheckers = null;

	private UserDetailsChecker postAuthenticationChecks = new DefaultPostAuthenticationChecks();

	public void afterPropertiesSet() throws Exception {
		if (ObjectUtil.isNull(userDetailsCheckers)) {
			userDetailsCheckers = new ArrayList<UserDetailsChecker>();
		}
	}

	public void check(UserDetails userDetails) {
		postAuthenticationChecks.check(userDetails);
		for (UserDetailsChecker userDetailsChecker : userDetailsCheckers) {
			userDetailsChecker.check(userDetails);
		}
	}

	public void setUserDetailsCheckers(List<UserDetailsChecker> userDetailsCheckers) {
		this.userDetailsCheckers = userDetailsCheckers;
	}

	private class DefaultPostAuthenticationChecks implements UserDetailsChecker {
		public void check(UserDetails user) {
			if (!user.isCredentialsNonExpired()) {
				LOGGER.debug("User account credentials have expired");
				throw new CredentialsExpiredException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.credentialsExpired", "User credentials have expired"));
			}
		}
	}

}
