package com.fantasy.framework.util.common;

import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import java.util.Enumeration;
import java.util.Map;

public class PropertiesHelperTest {

    private static final Log logger = LogFactory.getLog(PropertiesHelperTest.class);

    @Test
    public void testLoad() throws Exception {

    }

    @Test
    public void testGetProperties() throws Exception {

    }

    @Test
    public void testSetProperties() throws Exception {

    }

    @Test
    public void testGetRequiredString() throws Exception {

    }

    @Test
    public void testGetNullIfBlank() throws Exception {

    }

    @Test
    public void testGetNullIfEmpty() throws Exception {

    }

    @Test
    public void testGetAndTryFromSystem() throws Exception {

    }

    @Test
    public void testGetInteger() throws Exception {

    }

    @Test
    public void testGetInt() throws Exception {

    }

    @Test
    public void testGetRequiredInt() throws Exception {

    }

    @Test
    public void testGetLong() throws Exception {

    }

    @Test
    public void testGetRequiredLong() throws Exception {

    }

    @Test
    public void testGetBoolean() throws Exception {

    }

    @Test
    public void testGetBoolean1() throws Exception {

    }

    @Test
    public void testGetRequiredBoolean() throws Exception {

    }

    @Test
    public void testGetFloat() throws Exception {

    }

    @Test
    public void testGetRequiredFloat() throws Exception {

    }

    @Test
    public void testGetDouble() throws Exception {

    }

    @Test
    public void testGetRequiredDouble() throws Exception {

    }

    @Test
    public void testSetProperty() throws Exception {

    }

    @Test
    public void testGetProperty() throws Exception {
        PropertiesHelper helper = PropertiesHelper.load("props/application.properties");

        //普通测试
        logger.debug(helper.getProperty("test"));
        Assert.assertEquals(helper.getProperty("test"), "limaofeng");

        //测试默认值
        logger.debug(helper.getProperty("test1", "limaofeng"));
        Assert.assertEquals(helper.getProperty("test1", "limaofeng"), "limaofeng");

        //使用环境变量
        System.setProperty("username", "limaofeng");
        logger.debug("System getProperty by key(username) = " + System.getProperty("username"));
        logger.debug("System getenv by key(username) = " + System.getenv("username"));
        logger.debug(helper.getProperty("testenv"));

        Assert.assertEquals(helper.getProperty("testenv"), "username=limaofeng");
        System.setProperty("username", "haolue");
        logger.debug(helper.getProperty("testenv"));

        Assert.assertEquals(helper.getProperty("testenv"), "username=haolue");
    }

    @Test
    public void testClear() throws Exception {
        PropertiesHelper helper = PropertiesHelper.load("props/application.properties");

        logger.debug(helper.getProperty("test"));
        Assert.assertEquals(helper.getProperty("test"), "limaofeng");

        helper.clear();

        logger.debug(helper.getProperty("test"));
        Assert.assertNull(helper.getProperty("test"));
    }

    @Test
    public void testEntrySet() throws Exception {
        PropertiesHelper helper = PropertiesHelper.load("props/application.properties");

        for (Map.Entry<Object, Object> entry : helper.getProperties().entrySet()) {
            logger.debug(entry.getKey() + " = " + entry.getValue());
        }

    }

    @Test
    public void testPropertyNames() throws Exception {
        PropertiesHelper helper = PropertiesHelper.load("props/application.properties");

        Enumeration<?> enumeration = helper.getProperties().propertyNames();
        while (enumeration.hasMoreElements()) {
            Object key = enumeration.nextElement();
            logger.debug(key + " = " + helper.getProperties().get(key));
        }
    }

    @Test
    public void testContains() throws Exception {
        PropertiesHelper helper = PropertiesHelper.load("props/application.properties");
        Assert.assertTrue(helper.getProperties().contains("limaofeng"));
    }

    @Test
    public void testContainsKey() throws Exception {
        PropertiesHelper helper = PropertiesHelper.load("props/application.properties");
        Assert.assertTrue(helper.getProperties().containsKey("test"));
    }

    @Test
    public void testContainsValue() throws Exception {
        PropertiesHelper helper = PropertiesHelper.load("props/application.properties");
        Assert.assertTrue(helper.getProperties().containsValue("limaofeng"));
    }

    @Test
    public void testElements() throws Exception {
        PropertiesHelper helper = PropertiesHelper.load("props/application.properties");
        Enumeration<?> enumeration = helper.getProperties().elements();
        while (enumeration.hasMoreElements()) {
            Object value = enumeration.nextElement();
            logger.debug(value);
        }
    }

    @Test
    public void testIsEmpty() throws Exception {
        PropertiesHelper helper = PropertiesHelper.load("props/application.properties");
        Assert.assertFalse(helper.getProperties().isEmpty());
        helper = PropertiesHelper.load("props/application_empty.properties");
        Assert.assertTrue(helper.getProperties().isEmpty());

    }

}