package org.jfantasy.security.web.intercept;

import org.jfantasy.security.data.SecurityStorage;
import org.jfantasy.security.matcher.UrlResourceRequestMatcher;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Collections;

public class SecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    private SecurityStorage storage;

    public SecurityMetadataSource(SecurityStorage storage){
        this.storage = storage;
    }

    public Collection<ConfigAttribute> getAttributes(Object filter) throws IllegalArgumentException {
        FilterInvocation filterInvocation = (FilterInvocation) filter;
        HttpServletRequest request = filterInvocation.getRequest();
        for (UrlResourceRequestMatcher matcher : storage.getRequestMatchers()) {
            if (matcher.matches(request)) {
                return matcher.getPermissions();
            }
        }
        return Collections.emptyList();
    }

    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return storage.getAllPermissions();
    }

    public boolean supports(Class<?> clazz) {
        return true;
    }

}