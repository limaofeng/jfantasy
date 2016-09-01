package org.jfantasy.framework.util.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class DateUtilTest {

    private static final Log LOGGER = LogFactory.getLog(DateUtilTest.class);

    @Before
    public void setUp() throws Exception {
        LOGGER.debug("TimeZone : " + TimeZone.getDefault().getID());
    }

    @Test
    public void formatRfc822Date() throws Exception {

    }

    @Test
    public void format() throws Exception {

    }

    @Test
    public void format1() throws Exception {

    }

    @Test
    public void format2() throws Exception {

    }

    @Test
    public void format3() throws Exception {

    }

    @Test
    public void format4() throws Exception {

    }

    @Test
    public void format5() throws Exception {

    }

    @Test
    public void format6() throws Exception {

    }

    @Test
    public void toDay() throws Exception {

    }

    @Test
    public void isSameDay() throws Exception {

    }

    @Test
    public void parse() throws Exception {

    }

    @Test
    public void parse1() throws Exception {

    }

    @Test
    public void parse2() throws Exception {

    }

    @Test
    public void parse3() throws Exception {

    }

    @Test
    public void dayInterval() throws Exception {
        Date day = DateUtil.now();
        long num = DateUtil.dayInterval(day, day);
        Assert.assertEquals(num, 0);
        LOGGER.debug(num);
    }

    @Test
    public void isWorkDay() throws Exception {

    }

    @Test
    public void roundTo() throws Exception {

    }

    @Test
    public void nextDay() throws Exception {

    }

    @Test
    public void nextThreeDate() throws Exception {

    }

    @Test
    public void nextHour() throws Exception {

    }

    @Test
    public void add() throws Exception {

    }

    @Test
    public void set() throws Exception {

    }

    @Test
    public void getActualMaximumTime() throws Exception {

    }

    @Test
    public void getActualMinimumTime() throws Exception {

    }

    @Test
    public void interval() throws Exception {

    }

    @Test
    public void intervalFormat() throws Exception {

    }

    @Test
    public void intervalFormat1() throws Exception {

    }

    @Test
    public void interval1() throws Exception {

    }

    @Test
    public void compare() throws Exception {

    }

    @Test
    public void addDay() throws Exception {

    }

    @Test
    public void first() throws Exception {

    }

    @Test
    public void last() throws Exception {

    }

    @Test
    public void next() throws Exception {

    }

    @Test
    public void prev() throws Exception {

    }

    @Test
    public void getLastDayOfWeek() throws Exception {

    }

    @Test
    public void getTimeField() throws Exception {

    }

    @Test
    public void getWeekOfYear() throws Exception {

    }

    @Test
    public void setTimeField() throws Exception {

    }

    @Test
    public void min() throws Exception {

    }

    @Test
    public void max() throws Exception {

    }

    @Test
    public void now() throws Exception {

    }

    @Test
    public void age() throws Exception {

    }

    @Test
    public void set1() throws Exception {

    }

    @Test
    public void fieldValue() throws Exception {

    }

    @Test
    public void testFormat() throws Exception {
        LOGGER.debug(" 当前日期 :" + DateUtil.format("yyyy-MM-dd"));

        LOGGER.debug(" 上下午 :" + DateUtil.format("a") + "/" + DateUtil.format("a", Locale.US));

        LOGGER.debug(" 时间 :" + DateUtil.format("zzzz") + "/" + DateUtil.format("zzzz", Locale.US));

        LOGGER.debug(" 星期 :" + DateUtil.format("EEE") + "/" + DateUtil.format("EEE", Locale.US));

        LOGGER.debug(" 月份 :" + DateUtil.format("MMM") + "/" + DateUtil.format("MMM", Locale.US));

        LOGGER.debug(" 当前日期 :" + DateUtil.format("yyyy-MM-dd'T'HH:mm:ss.SSSZ") + "/" + DateUtil.format("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US));

        LOGGER.debug(" 当前日期 :" + DateUtil.formatRfc822Date(DateUtil.now()));
    }

}