package org.jfantasy.security.matcher;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfantasy.oauth.PermissionRule;
import org.jfantasy.oauth.UrlResource;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class UrlResourceRequestMatcher implements RequestMatcher {
    private final Log logger = LogFactory.getLog(getClass());
    private final List<RequestMatcher> requestMatchers;
    private UrlResource urlResource;
    private PermissionRule matched;

    public List<ConfigAttribute> getPermissions() {
        return this.matched.getSecurityConfigs();
    }

    public UrlResourceRequestMatcher(UrlResource urlResource) {
        this.urlResource = urlResource;
        List<RequestMatcher> requestMatchers = new ArrayList<>();
        for (PermissionRule rule : urlResource.getRules()) {
            requestMatchers.add(new AntPathRequestMatcher(rule.getPattern(), "*".equals(rule.getMethod()) ? null : rule.getMethod().toUpperCase()));
        }
        Assert.notEmpty(requestMatchers, "requestMatchers must contain a value");
        if (requestMatchers.contains(null)) {
            throw new IllegalArgumentException("requestMatchers cannot contain null values");
        }
        this.requestMatchers = requestMatchers;
    }

    public boolean matches(HttpServletRequest request) {
        int i = -1;
        for (RequestMatcher matcher : requestMatchers) {
            if (logger.isDebugEnabled()) {
                logger.debug("Trying to match using " + matcher);
            }
            i++;
            if (matcher.matches(request)) {
                logger.debug("matched");
                this.matched = urlResource.getRules().get(i);
                return true;
            }
        }
        logger.debug("No matches found");
        return false;
    }

    @Override
    public String toString() {
        return "UrlResourceRequestMatcher [requestMatchers=" + requestMatchers + "]";
    }

}