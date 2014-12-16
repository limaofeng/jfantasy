package com.fantasy.framework.util.web;

import com.fantasy.framework.util.web.context.ActionContext;
import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockServletContext;

import javax.servlet.http.Cookie;
import java.util.Arrays;
import java.util.Map;

public class WebUtilTest {

    private final static Log LOG = LogFactory.getLog(WebUtilTest.class);

    protected MockHttpServletRequest request;
    protected MockHttpServletResponse response;

    @Before
    public void setUp() throws Exception {
        request = new MockHttpServletRequest("get", "/test/test.jsp");
        request.setServerName("www.jfantasy.org");
        request.addHeader("Referer", "http://www.haoluesoft.com.cn");
        request.addHeader("Accept-Encoding", "gzip");

        request.addHeader("copyright", "jfantasy");
        request.addHeader("User-Agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10.10; rv:33.0) Gecko/20100101 Firefox/33.0");

        request.setCookies(new Cookie("username", "limaofeng"));

        response = new MockHttpServletResponse();

        ActionContext.getContext(request,response);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetExtension() throws Exception {
        Assert.assertEquals("jsp", WebUtil.getExtension(request));

        Assert.assertEquals("do", WebUtil.getExtension("/test/test.do"));
    }

    @Test
    public void testGetServerName() throws Exception {
        Assert.assertEquals("www.jfantasy.org", WebUtil.getServerName(request));

        Assert.assertEquals("www.jfantasy.org", WebUtil.getServerName("http://www.jfantasy.org/test.do"));
    }

    @Test
    public void testGetScheme() throws Exception {
        Assert.assertEquals("http", WebUtil.getScheme("http://www.jfantasy.org/test.do"));

        Assert.assertEquals("https", WebUtil.getScheme("https://www.jfantasy.org/test.do"));
    }

    @Test
    public void testGetPort() throws Exception {
        Assert.assertEquals(80, WebUtil.getPort(request));

        Assert.assertEquals(80, WebUtil.getPort("http://www.jfantasy.org/test.do"));

        Assert.assertEquals(443, WebUtil.getPort("https://www.jfantasy.org/test.do"));

        Assert.assertEquals(8080, WebUtil.getPort("https://www.jfantasy.org:8080/test.do"));
    }

    @Test
    public void testAcceptEncoding() throws Exception {
        Assert.assertEquals("gzip", WebUtil.getAcceptEncoding(request));
    }

    @Test
    public void testGetReferer() throws Exception {
        Assert.assertEquals("http://www.haoluesoft.com.cn", WebUtil.getReferer(request));
    }

    @Test
    public void testGetCookie() throws Exception {
        Cookie cookie = WebUtil.getCookie(request, "username");
        Assert.assertEquals("limaofeng", cookie.getValue());
    }

    @Test
    public void testAddCookie() throws Exception {
        WebUtil.addCookie(response, "email", "limaofeng@msn.com", 100);
        Assert.assertEquals("limaofeng@msn.com", response.getCookie("email").getValue());
    }

    @Test
    public void testRemoveCookie() throws Exception {
        WebUtil.removeCookie(request, response, "username");
        Cookie cookie = response.getCookie("username");
        Assert.assertEquals(0, cookie.getMaxAge());
    }

    @Test
    public void testGetRealIpAddress() throws Exception {
        Assert.assertEquals("127.0.0.1", WebUtil.getRealIpAddress(request));
    }

    @Test
    public void testIsSelfIp() throws Exception {
        Assert.assertTrue(WebUtil.isSelfIp("192.168.1.200"));
    }

    @Test
    public void testGetServerIps() throws Exception {
        String[] serverIps = WebUtil.getServerIps();
        Assert.assertTrue(Arrays.asList(serverIps).indexOf("192.168.1.200") != -1);
    }

    @Test
    public void testBrowser() throws Exception {
        WebUtil.Browser browser = WebUtil.browser(request);
        LOG.debug(browser);
    }

    @Test
    public void testGetBrowserVersion() throws Exception {
        WebUtil.Browser browser = WebUtil.browser(request);

        LOG.debug(WebUtil.getBrowserVersion(browser, request));
    }

    @Test
    public void testGetOsVersion() throws Exception {
        LOG.debug(WebUtil.getOsVersion(request));
    }

    @Test
    public void testParseQuery() throws Exception {
        Map<String, String[]> params = WebUtil.parseQuery("username=limaofeng&email=limaofeng@msn.com&arrays=1&arrays=2");

        Assert.assertEquals(params.get("username")[0], "limaofeng");
        Assert.assertEquals(params.get("email")[0], "limaofeng@msn.com");
        Assert.assertEquals(params.get("arrays").length, 2);
        Assert.assertEquals(params.get("arrays")[0], "1");
        Assert.assertEquals(params.get("arrays")[1], "2");

        TUser tUser = WebUtil.parseQuery("username=limaofeng&email=limaofeng@msn.com&arrays=1&arrays=2", TUser.class);

        Assert.assertEquals(tUser.getUsername(), "limaofeng");
        Assert.assertEquals(tUser.getEmail(), "limaofeng@msn.com");
        Assert.assertEquals(tUser.getArrays().length, 2);
        Assert.assertEquals(tUser.getArrays()[0], 1);
        Assert.assertEquals(tUser.getArrays()[1], 2);
    }

    public static class TUser {
        private String username;
        private String email;
        private int[] arrays;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public int[] getArrays() {
            return arrays;
        }

        public void setArrays(int[] arrays) {
            this.arrays = arrays;
        }
    }

    @Test
    public void testGetQueryString() throws Exception {
        String url = "username=limaofeng&email=limaofeng@msn.com&arrays=1&arrays=2";
        Map<String, String[]> params = WebUtil.parseQuery(url);

        String queryUrl = WebUtil.getQueryString(params);

        LOG.debug(queryUrl);

        Assert.assertEquals(url, queryUrl);
    }

    @Test
    public void testSort() throws Exception {
        String queryUrl = WebUtil.sort("username=limaofeng&email=limaofeng@msn.com&arrays=1&arrays=2", "username");
        LOG.debug(queryUrl);
        Assert.assertTrue(queryUrl.contains("username-asc"));

        queryUrl = WebUtil.sort(queryUrl, "username");
        LOG.debug(queryUrl);
        Assert.assertTrue(queryUrl.contains("username-desc"));
    }

    @Test
    public void testFilename() throws Exception {
        String filename = WebUtil.filename(new String("测试文件名称".getBytes(), "iso8859-1"));
        LOG.debug(filename);
    }

    @Test
    public void testTransformCoding() throws Exception {
        String filename = WebUtil.transformCoding(new String("测试文件名称".getBytes(), "iso8859-1"), "iso8859-1", "uft-8");
        LOG.debug(filename);
    }

    @Test
    public void testIsAjax() throws Exception {
        Assert.assertFalse(WebUtil.isAjax(request));

        request.addHeader("X-Requested-With", "XMLHttpRequest");

        Assert.assertTrue(WebUtil.isAjax(request));
    }

    @Test
    public void testGetSessionId() throws Exception {
        request.setSession(new MockHttpSession(new MockServletContext(), "TESTSESSIONID"));

        LOG.debug(WebUtil.getSessionId(request));

        Assert.assertEquals("TESTSESSIONID", WebUtil.getSessionId(request));
    }

    @Test
    public void testGetMethod() throws Exception {
        Assert.assertEquals("get", WebUtil.getMethod(request));
    }

    @Test
    public void testGetRequestUrl() throws Exception {
        LOG.debug(WebUtil.getRequestUrl(request));
        Assert.assertEquals("http://www.jfantasy.org", WebUtil.getRequestUrl(request));
    }

}