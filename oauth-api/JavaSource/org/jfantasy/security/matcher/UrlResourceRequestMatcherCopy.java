package org.jfantasy.security.matcher;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

public class UrlResourceRequestMatcherCopy implements RequestMatcher {
    private static final Log logger = LogFactory.getLog(UrlResourceRequestMatcherCopy.class);
    private static final String MATCH_ALL = "/**";

    private final UrlResourceRequestMatcherCopy.Matcher matcher;
    private final String pattern;
    private final HttpMethod httpMethod;
    private final boolean caseSensitive;

    public UrlResourceRequestMatcherCopy(String pattern) {
        this(pattern, null);
    }

    public UrlResourceRequestMatcherCopy(String pattern, String httpMethod) {
        this(pattern, httpMethod, false);
    }

    public UrlResourceRequestMatcherCopy(String pattern, String httpMethod, boolean caseSensitive) {
        Assert.hasText(pattern, "Pattern cannot be null or empty");
        this.caseSensitive = caseSensitive;

        if (pattern.equals(MATCH_ALL) || pattern.equals("**")) {
            pattern = MATCH_ALL;
            matcher = null;
        } else {
            if (!caseSensitive) {
                pattern = pattern.toLowerCase();
            }

            if (pattern.endsWith(MATCH_ALL)
                    && (pattern.indexOf('?') == -1 && pattern.indexOf('{') == -1 && pattern
                    .indexOf('}') == -1)
                    && pattern.indexOf("*") == pattern.length() - 2) {
                matcher = new UrlResourceRequestMatcherCopy.SubpathMatcher(pattern.substring(0, pattern.length() - 3));
            } else {
                matcher = new UrlResourceRequestMatcherCopy.SpringAntMatcher(pattern);
            }
        }

        this.pattern = pattern;
        this.httpMethod = StringUtils.hasText(httpMethod) ? HttpMethod
                .valueOf(httpMethod) : null;
    }

    public boolean matches(HttpServletRequest request) {
        if (httpMethod != null && StringUtils.hasText(request.getMethod())
                && httpMethod != valueOf(request.getMethod())) {
            if (logger.isDebugEnabled()) {
                logger.debug("Request '" + request.getMethod() + " "
                        + getRequestPath(request) + "'" + " doesn't match '" + httpMethod
                        + " " + pattern);
            }

            return false;
        }

        if (pattern.equals(MATCH_ALL)) {
            if (logger.isDebugEnabled()) {
                logger.debug("Request '" + getRequestPath(request)
                        + "' matched by universal pattern '/**'");
            }

            return true;
        }

        String url = getRequestPath(request);

        if (logger.isDebugEnabled()) {
            logger.debug("Checking match of request : '" + url + "'; against '" + pattern
                    + "'");
        }

        return matcher.matches(url);
    }

    private String getRequestPath(HttpServletRequest request) {
        String url = request.getServletPath();

        if (request.getPathInfo() != null) {
            url += request.getPathInfo();
        }

        if (!caseSensitive) {
            url = url.toLowerCase();
        }

        return url;
    }

    public String getPattern() {
        return pattern;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof UrlResourceRequestMatcherCopy)) {
            return false;
        }

        UrlResourceRequestMatcherCopy other = (UrlResourceRequestMatcherCopy) obj;
        return this.pattern.equals(other.pattern) && this.httpMethod == other.httpMethod
                && this.caseSensitive == other.caseSensitive;
    }

    @Override
    public int hashCode() {
        int code = 31 ^ pattern.hashCode();
        if (httpMethod != null) {
            code ^= httpMethod.hashCode();
        }
        return code;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Ant [pattern='").append(pattern).append("'");

        if (httpMethod != null) {
            sb.append(", ").append(httpMethod);
        }

        sb.append("]");

        return sb.toString();
    }

    private static HttpMethod valueOf(String method) {
        try {
            return HttpMethod.valueOf(method);
        } catch (IllegalArgumentException e) {
        }

        return null;
    }

    private static interface Matcher {
        boolean matches(String path);
    }

    private static class SpringAntMatcher implements UrlResourceRequestMatcherCopy.Matcher {
        private static final AntPathMatcher antMatcher = new AntPathMatcher();

        private final String pattern;

        private SpringAntMatcher(String pattern) {
            this.pattern = pattern;
        }

        public boolean matches(String path) {
            return antMatcher.match(pattern, path);
        }
    }

    private static class SubpathMatcher implements UrlResourceRequestMatcherCopy.Matcher {
        private final String subpath;
        private final int length;

        private SubpathMatcher(String subpath) {
            assert !subpath.contains("*");
            this.subpath = subpath;
            this.length = subpath.length();
        }

        public boolean matches(String path) {
            return path.startsWith(subpath)
                    && (path.length() == length || path.charAt(length) == '/');
        }
    }
}