package org.jfantasy.common.bean.converter;

import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfantasy.framework.util.web.context.ActionContext;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.HashMap;

public class HtmlConverterTest {

    private final static Log LOG = LogFactory.getLog(HtmlConverterTest.class);

    @Before
    public void setUp() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        ActionContext.getContext(request,response);
    }

    @Test
    public void testConvertValue() throws Exception {
        HtmlConverter htmlConverter = new HtmlConverter();
        String html = (String) htmlConverter.convertValue(new HashMap(), new Object(), null, "test", "我对贵公司感兴趣，<br/><span>希望探讨合作机会。</span>", String.class);
        LOG.debug(html);
        Assert.assertEquals(html,"我对贵公司感兴趣，<br /><span>希望探讨合作机会。</span>");
    }

}