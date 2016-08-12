package org.jfantasy.framework.web.filter;

import org.jfantasy.framework.web.filter.wrapper.CharacterEncodingRequestWrapper;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 终极乱码解决方法
 */
public class ConversionCharacterEncodingFilter extends OncePerRequestFilter {

    private final static String TRANSFORM = "ConversionCharacterEncodingFilter.transform";

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Boolean transform = (Boolean) request.getAttribute(TRANSFORM);
        if (transform == null) {
            filterChain.doFilter(new CharacterEncodingRequestWrapper(request), response);
            request.setAttribute(TRANSFORM, Boolean.TRUE);
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
