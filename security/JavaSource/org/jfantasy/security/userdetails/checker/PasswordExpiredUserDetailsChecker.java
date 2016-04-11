package org.jfantasy.security.userdetails.checker;

import org.jfantasy.security.userdetails.FantasyUserDetails;
import org.jfantasy.security.userdetails.SimpleUser;
import org.jfantasy.security.userdetails.exception.FirstLoginAuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;

/**
 * 密码过期验证
 *
 * @author Administrator
 */
public class PasswordExpiredUserDetailsChecker implements UserDetailsChecker {

    @SuppressWarnings({"unchecked", "unused"})
    public void check(UserDetails userDetails) {
        SimpleUser<FantasyUserDetails> simpleUser = (SimpleUser<FantasyUserDetails>) userDetails;
        // 每90天需要换新密码!
        throw new FirstLoginAuthenticationException("密码过期(密码有效期为90天)，请修改密码!");
    }

}
