package org.jfantasy.framework.httpclient;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class HttpClientUtilTest {

    private static final Log logger = LogFactory.getLog(HttpClientUtilTest.class);

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testDoGet() throws Exception {
        Response response = HttpClientUtil.doGet("http://www.baidu.com");
        logger.debug(response.getBody());
    }

    @Test
    public void testDoPost() throws Exception {

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("dir", "imageWx");
        data.put("attach", new File("/Users/limaofeng/framework/README.md"));
        Response response = HttpClientUtil.doPost("http://120.132.61.204:3080/files", data);
        logger.debug(response.getBody());
    }

}