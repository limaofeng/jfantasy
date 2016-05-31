package org.springframework.security.web.authentication;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Log LOG = LogFactory.getLog(RestAuthenticationEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        LOG.debug(authException.getMessage(), authException);
        response.setStatus(401);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println("{message:\"请登录后,访问该资源\"}");
        response.getWriter().flush();
    }

}
