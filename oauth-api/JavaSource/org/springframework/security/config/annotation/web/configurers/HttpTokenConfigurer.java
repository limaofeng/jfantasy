package org.springframework.security.config.annotation.web.configurers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.DelegatingAuthenticationEntryPoint;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.authentication.www.TokenAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.accept.ContentNegotiationStrategy;
import org.springframework.web.accept.HeaderContentNegotiationStrategy;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.LinkedHashMap;


public final class HttpTokenConfigurer<B extends HttpSecurityBuilder<B>> extends AbstractHttpConfigurer<HttpTokenConfigurer<B>, B> {
    private static final String DEFAULT_REALM = "Realm";

    private AuthenticationEntryPoint authenticationEntryPoint;
    private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource;
    private TokenAuthenticationEntryPoint tokenAuthEntryPoint = new TokenAuthenticationEntryPoint();

    /**
     * Creates a new instance
     * @throws Exception
     * @see HttpSecurity#httpBasic()
     */
    public HttpTokenConfigurer() throws Exception {
        realmName(DEFAULT_REALM);

        LinkedHashMap<RequestMatcher, AuthenticationEntryPoint> entryPoints = new LinkedHashMap<RequestMatcher, AuthenticationEntryPoint>();
        entryPoints.put(new RequestHeaderRequestMatcher("X-Requested-With","XMLHttpRequest"), new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));

        DelegatingAuthenticationEntryPoint defaultEntryPoint = new DelegatingAuthenticationEntryPoint(entryPoints);
        defaultEntryPoint.setDefaultEntryPoint(tokenAuthEntryPoint);
        authenticationEntryPoint = defaultEntryPoint;
    }

    public HttpTokenConfigurer<B> realmName(String realmName) throws Exception {
        tokenAuthEntryPoint.setRealmName(realmName);
        tokenAuthEntryPoint.afterPropertiesSet();
        return this;
    }

    public HttpTokenConfigurer<B> authenticationEntryPoint(AuthenticationEntryPoint authenticationEntryPoint) {
        this.authenticationEntryPoint = authenticationEntryPoint;
        return this;
    }

    public HttpTokenConfigurer<B> authenticationDetailsSource(
            AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource) {
        this.authenticationDetailsSource = authenticationDetailsSource;
        return this;
    }

    public void init(B http) throws Exception {
        registerDefaultAuthenticationEntryPoint(http);
    }

    @SuppressWarnings("unchecked")
    private void registerDefaultAuthenticationEntryPoint(B http) {
        ExceptionHandlingConfigurer<B> exceptionHandling = http.getConfigurer(ExceptionHandlingConfigurer.class);
        if (exceptionHandling == null) {
            return;
        }
        ContentNegotiationStrategy contentNegotiationStrategy = http.getSharedObject(ContentNegotiationStrategy.class);
        if (contentNegotiationStrategy == null) {
            contentNegotiationStrategy = new HeaderContentNegotiationStrategy();
        }
        MediaTypeRequestMatcher preferredMatcher = new MediaTypeRequestMatcher(
                contentNegotiationStrategy, MediaType.APPLICATION_ATOM_XML,
                MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_JSON,
                MediaType.APPLICATION_OCTET_STREAM, MediaType.APPLICATION_XML,
                MediaType.MULTIPART_FORM_DATA, MediaType.TEXT_XML);
        preferredMatcher.setIgnoredMediaTypes(Collections.singleton(MediaType.ALL));
        exceptionHandling.defaultAuthenticationEntryPointFor(
                postProcess(authenticationEntryPoint), preferredMatcher);

    }

    @Override
    public void configure(B http) throws Exception {
        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
        BasicAuthenticationFilter basicAuthenticationFilter = new BasicAuthenticationFilter(authenticationManager, authenticationEntryPoint);
        if (authenticationDetailsSource != null) {
            basicAuthenticationFilter.setAuthenticationDetailsSource(authenticationDetailsSource);
        }
        RememberMeServices rememberMeServices = http.getSharedObject(RememberMeServices.class);
        if(rememberMeServices != null) {
            basicAuthenticationFilter.setRememberMeServices(rememberMeServices);
        }
        basicAuthenticationFilter = postProcess(basicAuthenticationFilter);
        http.addFilter(basicAuthenticationFilter);
    }
}