package com.fantasy.security.web.authentication;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider implements InitializingBean {

	private final static Log log = LogFactory.getLog(UserDetailsAuthenticationProvider.class);

	private DaoAuthenticationProvider daoProvider;

	private Method additionalAuthenticationChecks;
	private Method retrieveUser;

	public void doAfterPropertiesSet() throws Exception {
		super.doAfterPropertiesSet();
		additionalAuthenticationChecks = DaoAuthenticationProvider.class.getDeclaredMethod("additionalAuthenticationChecks", UserDetails.class, UsernamePasswordAuthenticationToken.class);
		additionalAuthenticationChecks.setAccessible(true);
		retrieveUser = DaoAuthenticationProvider.class.getDeclaredMethod("retrieveUser", String.class, UsernamePasswordAuthenticationToken.class);
		retrieveUser.setAccessible(true);
	}

	@Override
	protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
		try {
			return (UserDetails) retrieveUser.invoke(daoProvider, username, authentication);
		} catch (IllegalArgumentException e) {
			log.error(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			log.error(e.getMessage(), e);
		} catch (InvocationTargetException e) {
			throw (AuthenticationException) e.getTargetException();
		}
		throw new UsernameNotFoundException(messages.getMessage("JdbcDaoImpl.notFound", new Object[] { username }, "Username {0} not found", Locale.CHINA));
	}

	public void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
		try {
			// 原来的验证方法
			additionalAuthenticationChecks.invoke(daoProvider, userDetails, authentication);
		} catch (IllegalArgumentException e) {
			log.error(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			log.error(e.getMessage(), e);
		} catch (InvocationTargetException e) {
			if (e.getTargetException() instanceof AuthenticationException) {
				throw (AuthenticationException) e.getTargetException();
			} else {
				log.error(e.getMessage(), e);
			}
		}
	}

	public void setDaoProvider(DaoAuthenticationProvider daoProvider) {
		this.daoProvider = daoProvider;
	}

}
