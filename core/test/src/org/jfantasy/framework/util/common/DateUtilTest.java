package org.jfantasy.framework.util.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;
import java.util.TimeZone;


public class DateUtilTest {

    private static final Log LOGGER = LogFactory.getLog(DateUtilTest.class);

    @Before
    public void setUp() throws Exception {
        LOGGER.debug("TimeZone : " + TimeZone.getDefault().getID());
    }

    @Test
    public void testFormat() throws Exception {
        LOGGER.debug(" 当前日期 :" + DateUtil.format("yyyy-MM-dd"));

        LOGGER.debug(" 上下午 :" + DateUtil.format("a") + "/" + DateUtil.format("a",Locale.US));

        LOGGER.debug(" 时间 :" + DateUtil.format("zzzz") + "/" + DateUtil.format("zzzz",Locale.US));

        LOGGER.debug(" 星期 :" + DateUtil.format("EEE") + "/" + DateUtil.format("EEE",Locale.US));

        LOGGER.debug(" 月份 :" + DateUtil.format("MMM") + "/" + DateUtil.format("MMM",Locale.US));

        LOGGER.debug(" 当前日期 :" + DateUtil.format("yyyy-MM-dd'T'HH:mm:ss.SSSZ") + "/" + DateUtil.format("yyyy-MM-dd'T'HH:mm:ss.SSSZ",Locale.US));

        LOGGER.debug(" 当前日期 :" + DateUtil.formatRfc822Date(DateUtil.now()));
    }

}