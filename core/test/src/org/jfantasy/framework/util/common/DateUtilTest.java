package org.jfantasy.framework.util.common;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import java.util.TimeZone;


public class DateUtilTest {

    private static final Logger LOGGER = Logger.getLogger(DateUtilTest.class);

    @Before
    public void setUp() throws Exception {
        LOGGER.debug("TimeZone : " + TimeZone.getDefault());
    }

    @Test
    public void testFormat() throws Exception {
        LOGGER.debug(" 当前日期 :" + DateUtil.format("yyyy-MM-dd"));
    }

}