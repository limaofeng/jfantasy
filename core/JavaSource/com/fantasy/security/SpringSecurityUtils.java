package com.fantasy.security;

import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.framework.util.common.ClassUtil;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.security.bean.Role;
import com.fantasy.security.bean.UserGroup;
import com.fantasy.security.userdetails.FantasyUserDetails;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SpringSecurityUtils {
	public static UserDetails getCurrentUser() {
		Authentication authentication = getAuthentication();
		if (authentication != null) {
			Object principal = authentication.getPrincipal();
			if ((principal instanceof UserDetails)) {
				return (UserDetails) principal;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static <T extends UserDetails> T getCurrentUser(Class<T> clazz) {
		Authentication authentication = getAuthentication();
		if (authentication != null) {
			Object principal = authentication.getPrincipal();
			if ((principal instanceof UserDetails)) {
				return clazz.cast(principal);
			}
		}
		return null;
	}

	public static String getCurrentUserName() {
		Authentication authentication = getAuthentication();
		if ((authentication != null) && (authentication.getPrincipal() != null)) {
			return authentication.getName();
		}
		return "";
	}

	public static String getCurrentUserIp() {
		Authentication authentication = getAuthentication();
		if (authentication != null) {
			Object details = authentication.getDetails();
			if ((details instanceof WebAuthenticationDetails)) {
				WebAuthenticationDetails webDetails = (WebAuthenticationDetails) details;
				return webDetails.getRemoteAddress();
			}
		}
		return "";
	}

	public static boolean hasAnyAuthority(String... authoritys) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			return false;
		}
		Collection<? extends GrantedAuthority> granteds = authentication.getAuthorities();
		if (granteds == null) {
			return false;
		}
        for (String a : authoritys) {
            for (GrantedAuthority authority : granteds) {
                if (a.equals(authority.getAuthority())) {
                    return true;
                }
            }
        }
		return false;
	}

	public static boolean hasAuthority(String authority) {
		return hasAnyAuthority(authority);
	}

	public static void saveUserDetailsToContext(UserDetails userDetails, HttpServletRequest request) {
		PreAuthenticatedAuthenticationToken authentication = new PreAuthenticatedAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
		authentication.setDetails(new WebAuthenticationDetails(request));
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	private static Authentication getAuthentication() {
		SecurityContext context = SecurityContextHolder.getContext();
		if (context != null) {
			return context.getAuthentication();
		}
		return null;
	}

	public static PasswordEncoder getPasswordEncoder() {
		DaoAuthenticationProvider daoProvider = SpringContextUtil.getBeanByType(DaoAuthenticationProvider.class);
		return (PasswordEncoder) ClassUtil.getValue(daoProvider, "passwordEncoder");
	}
	
	public static Collection<GrantedAuthority> getAuthorities(FantasyUserDetails fantasyUserDetails){
		List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
		// 添加用户组权限
		for (UserGroup userGroup : fantasyUserDetails.getUserGroups()) {
			if (!userGroup.isEnabled()){
                continue;
            }
			ObjectUtil.join(grantedAuthorities, userGroup.getGroupAuthorities(), "authority");
			ObjectUtil.join(grantedAuthorities, userGroup.getUrlAuthorities(), "authority");
			ObjectUtil.join(grantedAuthorities, userGroup.getMenuAuthorities(), "authority");
			ObjectUtil.join(grantedAuthorities, userGroup.getRoleAuthorities(), "authority");
		}
		// 添加角色权限
		for (Role role : fantasyUserDetails.getRoles()) {
			ObjectUtil.join(grantedAuthorities, role.getUrlAuthorities(), "authority");
			ObjectUtil.join(grantedAuthorities, role.getRoleAuthorities(), "authority");
			ObjectUtil.join(grantedAuthorities, role.getMenuAuthorities(), "authority");
		}
		return grantedAuthorities;
	}
}
