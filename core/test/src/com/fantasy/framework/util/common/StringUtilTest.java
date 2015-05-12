package com.fantasy.framework.util.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Test;

import java.util.UUID;

public class StringUtilTest {

    private static final Log logger = LogFactory.getLog(StringUtilTest.class);

    @Test
    public void testEllipsis() throws Exception {
        String str = "iVBORw0KGgoAAAANSUhEUgAAATgAAAEaCAYAAACFG0tXAAAKQWlDQ1BJQ0MgUHJvZmlsZQAASA2d";

        String newStr = StringUtil.ellipsis(str,20,"...");

        logger.debug(" StringUtil.ellipsis(str,20,\"...\") ==> " + newStr);

        Assert.assertEquals(str.substring(0,17) + "...",newStr);

    }

    @Test
    public void testLength() throws Exception {

    }

    @Test
    public void testIsChinese() throws Exception {

    }

    @Test
    public void testTrim() throws Exception {

    }

    @Test
    public void testSplit() throws Exception {

    }

    @Test
    public void testIsNull() throws Exception {

    }

    @Test
    public void testIsNotNull() throws Exception {

    }

    @Test
    public void testNullValue() throws Exception {

    }

    @Test
    public void testDefaultValue() throws Exception {

    }

    @Test
    public void testLongValue() throws Exception {

    }

    @Test
    public void testNoNull() throws Exception {

    }

    @Test
    public void testIsBlank() throws Exception {

    }

    @Test
    public void testIsNotBlank() throws Exception {

    }

    @Test
    public void testUpperCaseFirst() throws Exception {

    }

    @Test
    public void testLowerCaseFirst() throws Exception {

    }

    @Test
    public void testAppend() throws Exception {

    }

    @Test
    public void testAddZeroLeft() throws Exception {

    }

    @Test
    public void testAddLeft() throws Exception {

    }

    @Test
    public void testIsNumber() throws Exception {

    }

    @Test
    public void testEncodeURI() throws Exception {

    }

    @Test
    public void testDecodeURI() throws Exception {

    }

    @Test
    public void testEscapeSpecialSign() throws Exception {

    }

    @Test
    public void testAsciiToLowerCase() throws Exception {

    }

    @Test
    public void testStartsWithIgnoreCase() throws Exception {

    }

    @Test
    public void testEndsWithIgnoreCase() throws Exception {

    }

    @Test
    public void testIndexFrom() throws Exception {

    }

    @Test
    public void testReplace() throws Exception {

    }

    @Test
    public void testAppend1() throws Exception {

    }

    @Test
    public void testAppend2digits() throws Exception {

    }

    @Test
    public void testJoin() throws Exception {

    }

    @Test
    public void testAdd() throws Exception {

    }

    @Test
    public void testNonNull() throws Exception {

    }

    @Test
    public void testEquals() throws Exception {

    }

    @Test
    public void testToUTF8String() throws Exception {

    }

    @Test
    public void testToString() throws Exception {

    }

    @Test
    public void testPrintable() throws Exception {

    }

    @Test
    public void testGetBytes() throws Exception {

    }

    @Test
    public void testGetBytes1() throws Exception {

    }

    @Test
    public void testSidBytesToString() throws Exception {

    }

    @Test
    public void testSidStringToBytes() throws Exception {

    }

    @Test
    public void testIsEmpty() throws Exception {

    }

    @Test
    public void testEscapeHtml() throws Exception {

    }

    @Test
    public void testEscapeJavaScript() throws Exception {

    }

    @Test
    public void testEscapeXml() throws Exception {

    }

    @Test
    public void testEscapeCsv() throws Exception {

    }

    @Test
    public void testTokenizeToStringArray() throws Exception {

    }

    @Test
    public void testTokenizeToStringArray1() throws Exception {

    }

    @Test
    public void testGetBirthday() throws Exception {

    }

    @Test
    public void testShortUrl() throws Exception {

    }

    @Test
    public void testHexTo64() throws Exception {
        logger.debug(StringUtil.hexTo64("0" + UUID.randomUUID().toString().replaceAll("-","")));
    }
}