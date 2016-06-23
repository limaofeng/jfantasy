package org.springframework.security.web.authentication.www;

import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class ToKenAuthenticationFilter extends OncePerRequestFilter {

    private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new WebAuthenticationDetailsSource();
    private AuthenticationEntryPoint authenticationEntryPoint;
    private AuthenticationManager authenticationManager;
    private boolean ignoreFailure = false;
    private String credentialsCharset = "UTF-8";

    public ToKenAuthenticationFilter(AuthenticationManager authenticationManager, AuthenticationEntryPoint authenticationEntryPoint) {
        Assert.notNull(authenticationManager, "authenticationManager cannot be null");
        Assert.notNull(authenticationEntryPoint, "authenticationEntryPoint cannot be null");
        this.authenticationManager = authenticationManager;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Override
    public void afterPropertiesSet() {
        Assert.notNull(this.authenticationManager, "An AuthenticationManager is required");

        if (!isIgnoreFailure()) {
            Assert.notNull(this.authenticationEntryPoint,"An AuthenticationEntryPoint is required");
        }
    }

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        final boolean debug = logger.isDebugEnabled();

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Token ")) {
            chain.doFilter(request, response);
            return;
        }

        try {
            String token = extractHeader(header, request);

            if (debug) {
                logger.debug("Basic Authentication Authorization header found for token '" + token + "'");
            }

            if (authenticationIsRequired(token)) {
                AccessTokenAuthenticationToken authRequest = new AccessTokenAuthenticationToken(token);
                authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
                Authentication authResult = authenticationManager.authenticate(authRequest);

                if (debug) {
                    logger.debug("Authentication success: " + authResult);
                }

                SecurityContextHolder.getContext().setAuthentication(authResult);

                onSuccessfulAuthentication(request, response, authResult);
            }

        } catch (AuthenticationException failed) {
            SecurityContextHolder.clearContext();

            if (debug) {
                logger.debug("Authentication request for failed: " + failed);
            }

            onUnsuccessfulAuthentication(request, response, failed);

            if (ignoreFailure) {
                chain.doFilter(request, response);
            } else {
                authenticationEntryPoint.commence(request, response, failed);
            }

            return;
        }

        chain.doFilter(request, response);
    }

    private String extractHeader(String header, HttpServletRequest request) throws IOException {
        return header.substring(6);
    }

    private boolean authenticationIsRequired(String username) {
        Authentication existingAuth = SecurityContextHolder.getContext().getAuthentication();

        return existingAuth == null || !existingAuth.isAuthenticated() || existingAuth instanceof UsernamePasswordAuthenticationToken && !existingAuth.getName().equals(username) || existingAuth instanceof AnonymousAuthenticationToken;

    }

    protected void onSuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authResult) throws IOException {
    }

    protected void onUnsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed)
            throws IOException {
    }

    protected AuthenticationEntryPoint getAuthenticationEntryPoint() {
        return authenticationEntryPoint;
    }

    protected AuthenticationManager getAuthenticationManager() {
        return authenticationManager;
    }

    protected boolean isIgnoreFailure() {
        return ignoreFailure;
    }

    public void setAuthenticationDetailsSource(AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource) {
        Assert.notNull(authenticationDetailsSource,"AuthenticationDetailsSource required");
        this.authenticationDetailsSource = authenticationDetailsSource;
    }

    public void setCredentialsCharset(String credentialsCharset) {
        Assert.hasText(credentialsCharset, "credentialsCharset cannot be null or empty");
        this.credentialsCharset = credentialsCharset;
    }

    protected String getCredentialsCharset(HttpServletRequest httpRequest) {
        return credentialsCharset;
    }
}