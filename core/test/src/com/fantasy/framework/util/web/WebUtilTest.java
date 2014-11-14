package com.fantasy.framework.util.web;

import com.fantasy.framework.util.regexp.RegexpUtil;
import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

public class WebUtilTest {

    private final static Log logger = LogFactory.getLog(WebUtilTest.class);

    @Test
    public void testGetExtension() throws Exception {

    }

    @Test
    public void testGetExtension1() throws Exception {

    }

    @Test
    public void testGetServerName() throws Exception {

    }

    @Test
    public void testGetScheme() throws Exception {

    }

    @Test
    public void testGetPort() throws Exception {

    }

    @Test
    public void testGetResponsePath() throws Exception {

    }

    @Test
    public void testAcceptEncoding() throws Exception {

    }

    @Test
    public void testGetReferer() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Referer","http://uc-vs.bdimg.com/v2/20141031x1/asset/common/css/main.css?v=20140317c");

        logger.debug(WebUtil.getReferer(request));
        Assert.assertEquals("http://uc-vs.bdimg.com/v2/20141031x1/asset/common/css/main.css?v=20140317c",WebUtil.getReferer(request));

        logger.debug(RegexpUtil.parseFirst(WebUtil.getReferer(request), "(http://|https://)[^/]+"));

    }

    @Test
    public void testGetCookie() throws Exception {

    }

    @Test
    public void testAddCookie() throws Exception {

    }

    @Test
    public void testRemoveCookie() throws Exception {

    }

    @Test
    public void testGetRealIpAddress() throws Exception {

    }

    @Test
    public void testIsSelfIp() throws Exception {

    }

    @Test
    public void testGetServerIps() throws Exception {

    }

    @Test
    public void testBrowser() throws Exception {

    }

    @Test
    public void testGetBrowserVersion() throws Exception {

    }

    @Test
    public void testGetOsVersion() throws Exception {

    }

    @Test
    public void testParseQuery() throws Exception {

    }

    @Test
    public void testGetQueryString() throws Exception {

    }

    @Test
    public void testSort() throws Exception {

    }

    @Test
    public void testFilename() throws Exception {

    }

    @Test
    public void testGetValue() throws Exception {

    }

    @Test
    public void testTransformCoding() throws Exception {

    }

    @Test
    public void testIsAjax() throws Exception {

    }

    @Test
    public void testGetSessionId() throws Exception {

    }

    @Test
    public void testGetMethod() throws Exception {

    }
}