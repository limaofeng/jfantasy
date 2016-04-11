package org.jfantasy.framework.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.tuckey.web.filters.urlrewrite.Conf;
import org.tuckey.web.filters.urlrewrite.RewrittenOutboundUrl;
import org.tuckey.web.filters.urlrewrite.RewrittenUrl;
import org.tuckey.web.filters.urlrewrite.UrlRewriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;

/**
 * 测试 urlrewrite
 */
public class UrlRewriterTest {

    private static final Log logger = LogFactory.getLog(UrlRewriterTest.class);
    private static final String REWRITE_CONF = "urlrewrite.xml";
    private Conf conf;
    private UrlRewriter rewriter;
    private Method processEncodeURLMethod;

    @Before
    public void setUp() throws MalformedURLException, SecurityException, NoSuchMethodException {
        // InputStream istream = getClass().getResourceAsStream(REWRITE_CONF);
        // conf = new Conf(istream, REWRITE_CONF);
        conf = new Conf(getClass().getResource(REWRITE_CONF));
        rewriter = new UrlRewriter(conf);
        processEncodeURLMethod = rewriter.getClass().getDeclaredMethod("processEncodeURL", HttpServletResponse.class, HttpServletRequest.class, boolean.class, String.class);
        processEncodeURLMethod.setAccessible(true);
    }

    private String encodeURL(String fromUrl) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", fromUrl);
        MockHttpServletResponse response = new MockHttpServletResponse();
        RewrittenOutboundUrl outboundUrl = (RewrittenOutboundUrl) processEncodeURLMethod.invoke(rewriter, response, request, false, fromUrl);// rewriter.processEncodeURL(response,request, false, fromUrl);
        return outboundUrl != null ? outboundUrl.getTarget() : null;
    }

    public String process(String fromUrl) throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", fromUrl);
        MockHttpServletResponse response = new MockHttpServletResponse();
        RewrittenUrl rewrittenUrl = rewriter.processRequest(request, response);
        return rewrittenUrl != null ? rewrittenUrl.getTarget() : null;
    }

    @Test
    public void urlrewrite() throws Exception {
        //logger.debug(encodeURL("/file/download.do?filePath=sdfsdfs.jpg"));
        logger.debug(process("/api/users"));
        logger.debug(process("/api/user/limaofeng"));

        logger.debug(process("/test-redirect-301"));



    }

}
