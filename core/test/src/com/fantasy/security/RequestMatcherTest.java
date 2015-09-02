package com.fantasy.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.web.util.matcher.*;
import org.springframework.web.accept.ParameterContentNegotiationStrategy;

import java.util.HashMap;

public class RequestMatcherTest {

    private final static Log LOG = LogFactory.getLog(RequestMatcherTest.class);

    @Test
    public void testMediaTypeRequestMatcher() {
        ParameterContentNegotiationStrategy strategy = new ParameterContentNegotiationStrategy(new HashMap<String, MediaType>() {
            {
                this.put("application/json", MediaType.APPLICATION_JSON);
            }
        });
        MediaTypeRequestMatcher matcher = new MediaTypeRequestMatcher(strategy, MediaType.APPLICATION_JSON);
        MockHttpServletRequest request = new MockHttpServletRequest("GET","http://loaclhost:8080/members");
        request.setContentType("application/json;charset=UTF-8");
        request.addParameter("format","kx");
        LOG.debug(matcher.matches(request));
    }

    @Test
    public void testIpAddressMatcher(){
        IpAddressMatcher matcher = new IpAddressMatcher("192.168.1.0/24");
        MockHttpServletRequest request = new MockHttpServletRequest("GET","http://192.168.1.24:8080/members");
        request.setRemoteAddr("192.168.2.91");
        LOG.debug(matcher.matches(request));
    }

    @Test
    public void testRegexRequestMatcher(){
        RegexRequestMatcher matcher = new RegexRequestMatcher("\\/members$","GET");
        MockHttpServletRequest request = new MockHttpServletRequest("GET","http://192.168.1.24:8080");
        request.setServletPath("/membe");
        LOG.debug(matcher.matches(request));
    }

    @Test
    public void testAntPathRequestMatcher(){
        AntPathRequestMatcher matcher = new AntPathRequestMatcher("/members/*","POST");
        MockHttpServletRequest request = new MockHttpServletRequest("GET","http://192.168.1.24:8080");
        request.setServletPath("/memberes/123");
        LOG.debug(matcher.matches(request));
    }

    @Test
    public void testOrRequestMatcher(){
        OrRequestMatcher matcher = new OrRequestMatcher(AnyRequestMatcher.INSTANCE);
        MockHttpServletRequest request = new MockHttpServletRequest("GET","http://192.168.1.24:8080");
        request.setServletPath("/memberes/123");
        LOG.debug(matcher.matches(request));
    }

    @Test
    public void testAndRequestMatcher(){
        AndRequestMatcher matcher = new AndRequestMatcher(AnyRequestMatcher.INSTANCE);
        MockHttpServletRequest request = new MockHttpServletRequest("GET","http://192.168.1.24:8080");
        request.setServletPath("/memberes/123");
        LOG.debug(matcher.matches(request));
    }

    @Test
    public void testELRequestMatcher(){
        ELRequestMatcher matcher = new ELRequestMatcher(" true ");
        MockHttpServletRequest request = new MockHttpServletRequest("GET","http://192.168.1.24:8080");
        request.setServletPath("/memberes/123");
        LOG.debug(matcher.matches(request));
    }

    @Test
    public void testRequestHeaderRequestMatcher(){
        RequestHeaderRequestMatcher matcher = new RequestHeaderRequestMatcher("Content-Type","application/json;");
        MockHttpServletRequest request = new MockHttpServletRequest("GET","http://192.168.1.24:8080");
        request.setContentType("application/json;charset=UTF-8");
        request.setServletPath("/memberes/123");
        LOG.debug(matcher.matches(request));
    }

}
