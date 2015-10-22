package com.fantasy.security.web.intercept;

import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.security.bean.Permission;
import com.fantasy.security.bean.Resource;
import com.fantasy.security.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    @Autowired
    private PermissionService permissionService;

    private Map<String, RequestMatcher> requestMatchers = new ConcurrentHashMap<String, RequestMatcher>();

    public Collection<ConfigAttribute> getAttributes(Object filter) throws IllegalArgumentException {
        FilterInvocation filterInvocation = (FilterInvocation) filter;
        return getGrantedAuthorities(filterInvocation.getHttpRequest());
    }

    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return new ArrayList<ConfigAttribute>();
    }

    public boolean supports(Class<?> clazz) {
        return true;
    }

    @SuppressWarnings("unchecked")
    public List<ConfigAttribute> getGrantedAuthorities(HttpServletRequest request) {
        Map<Resource, List<Permission>> urlAuthorities = this.permissionService.loadPermissions();
        for (Map.Entry<Resource, List<Permission>> entry : urlAuthorities.entrySet()) {
            Resource resource = entry.getKey();
            if (getRequestMatcher(resource).matches(request)) {
                for (Permission permission : entry.getValue()) {
                    if (getRequestMatcher(permission).matches(request)) {
                        return permission.getAuthorities();
                    }
                }
            }
        }
        return Collections.emptyList();
    }

    private RequestMatcher getRequestMatcher(Resource resource) {
        String key = "resource_" + resource.getId();
        if (requestMatchers.containsKey(key)) {
            return requestMatchers.get(key);
        }
        List<RequestMatcher> matchers = new ArrayList<RequestMatcher>();
        for (String url : StringUtil.tokenizeToStringArray(resource.getValue())) {
            matchers.add(new AntPathRequestMatcher(url));
        }
        requestMatchers.put(key, new OrRequestMatcher(matchers));
        return requestMatchers.get(key);
    }

    private RequestMatcher getRequestMatcher(Permission permission) {
        String key = "authority_" + permission.getId();
        if (requestMatchers.containsKey(key)) {
            return requestMatchers.get(key);
        }
        requestMatchers.put(key, permission.getRequestMatcher());
        return requestMatchers.get(key);
    }

}