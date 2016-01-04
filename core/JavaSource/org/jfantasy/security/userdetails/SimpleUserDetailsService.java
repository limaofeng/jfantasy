package org.jfantasy.security.userdetails;

import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.security.bean.User;
import org.jfantasy.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Locale;

public class SimpleUserDetailsService implements UserDetailsService {

    protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

    @Autowired
    private UserService userService;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findUniqueByUsername(username);
        if (ObjectUtil.isNull(user)) {
            throw new UsernameNotFoundException(messages.getMessage("JdbcDaoImpl.notFound", new Object[]{username}, "Username {0} not found", Locale.CANADA));
        }
        return new AdminUser(user);
    }

}
