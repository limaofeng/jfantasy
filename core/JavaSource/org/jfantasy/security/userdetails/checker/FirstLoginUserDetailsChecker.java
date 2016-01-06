package org.jfantasy.security.userdetails.checker;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;

import org.jfantasy.framework.util.web.context.ActionContext;
import org.jfantasy.security.userdetails.FantasyUserDetails;
import org.jfantasy.security.userdetails.SimpleUser;
import org.jfantasy.security.userdetails.exception.FirstLoginAuthenticationException;

/**
 * 是否首次登陆验证
 *
 * @author Administrator
 */
public class FirstLoginUserDetailsChecker implements UserDetailsChecker {

    @SuppressWarnings("unchecked")
    public void check(UserDetails userDetails) {
        SimpleUser<FantasyUserDetails> simpleUser = (SimpleUser<FantasyUserDetails>) userDetails;
        HttpServletRequest request = ActionContext.getContext().getHttpRequest();
        request.getSession().setAttribute("SPRING_SECURITY_LAST_USERID", simpleUser.getUser().getUsername());
        throw new FirstLoginAuthenticationException("首次登陆请先修改密码!");
    }

}
