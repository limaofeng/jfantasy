package com.fantasy.framework.web.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class CorsFilter implements Filter {

    private String corsAllowedMethods;
    private String corsAllowedHeaders;

    public void init(FilterConfig filterConfig) {
        corsAllowedMethods = filterConfig.getInitParameter("cors.allowed.methods");
        filterConfig.getInitParameter("cors.allowed.headers");
    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
        chain.doFilter(req, res);
    }

    public void destroy() {
    }
}
