package org.jfantasy.aliyun.oss;

import org.apache.commons.httpclient.Header;
import org.apache.log4j.Logger;
import org.jfantasy.framework.httpclient.HttpClientUtil;
import org.jfantasy.framework.httpclient.Response;
import org.junit.Test;

import java.io.IOException;

public class DNSTest {

    private static final Logger LOGGER = Logger.getLogger(DNSTest.class);

    @Test
    public void getImage() {
        try {
            Response response = HttpClientUtil.doGet("http://static.jfantasy.org/image/png/03pRLAMA3PrlIJZC2dtu41.png");
            for (Header header : response.getResponseHeaders()) {
                LOGGER.debug(header.getName() + " : " + header.getValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
