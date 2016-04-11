package org.jfantasy.security.userdetails;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;

import java.util.ArrayList;
import java.util.List;

/**
 * spring security 登陆前置验证
 *
 * @author 李茂峰
 */
public class PreAuthenticationChecks implements UserDetailsChecker, InitializingBean {

    protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

    protected final static Log LOGGER = LogFactory.getLog(PostAuthenticationChecks.class);

    private List<UserDetailsChecker> userDetailsCheckers = null;

    private UserDetailsChecker preAuthenticationChecks = new DefaultPreAuthenticationChecks();

    public void afterPropertiesSet() throws Exception {
        if (ObjectUtil.isNull(userDetailsCheckers)) {
            userDetailsCheckers = new ArrayList<UserDetailsChecker>();
        }
    }

    public void check(UserDetails userDetails) {
        preAuthenticationChecks.check(userDetails);
        for (UserDetailsChecker userDetailsChecker : userDetailsCheckers) {
            userDetailsChecker.check(userDetails);
        }
    }

    public void setUserDetailsCheckers(List<UserDetailsChecker> userDetailsCheckers) {
        this.userDetailsCheckers = userDetailsCheckers;
    }

    private class DefaultPreAuthenticationChecks implements UserDetailsChecker {

        public void check(UserDetails user) {
            if (!user.isAccountNonLocked()) {
                LOGGER.debug("User account is locked");
                throw new LockedException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.locked", "User account is locked"));
            }
            if (!user.isEnabled()) {
                LOGGER.debug("User account is disabled");
                throw new DisabledException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.disabled", "User is disabled"));
            }
            if (!user.isAccountNonExpired()) {
                LOGGER.debug("User account is expired");
                throw new AccountExpiredException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.expired", "User account has expired"));
            }
        }
    }

}