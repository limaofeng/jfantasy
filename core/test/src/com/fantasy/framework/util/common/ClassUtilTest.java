package com.fantasy.framework.util.common;

import com.fantasy.security.bean.User;
import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import java.lang.reflect.Array;

public class ClassUtilTest {

    private static Log LOG = LogFactory.getLog(ClassUtilTest.class);

    @Test
    public void testLoadClass() throws Exception {

    }

    @Test
    public void testGetBeanInfo() throws Exception {

    }

    @Test
    public void testNewInstance() throws Exception {

    }

    @Test
    public void testGetRealClass() throws Exception {
    }

    @Test
    public void testNewInstance1() throws Exception {

    }

    @Test
    public void testGetPropertys() throws Exception {

    }

    @Test
    public void testGetProperty() throws Exception {

    }

    @Test
    public void testForName() throws Exception {
        String className = Array.newInstance(User.class,0).getClass().getName();

        Class<?> clazz = ClassUtil.forName(className);

        LOG.debug(clazz);

        Assert.assertEquals(clazz,Array.newInstance(User.class,0).getClass());

    }

    @Test
    public void testGetValue() throws Exception {

    }

    @Test
    public void testGetDeclaredField() throws Exception {

    }

    @Test
    public void testSetValue() throws Exception {

    }

    @Test
    public void testGetMethodProxy() throws Exception {

    }

    @Test
    public void testIsBasicType() throws Exception {

    }

    @Test
    public void testIsArray() throws Exception {

    }

    @Test
    public void testIsInterface() throws Exception {

    }

    @Test
    public void testIsList() throws Exception {

    }

    @Test
    public void testIsMap() throws Exception {

    }

    @Test
    public void testIsList1() throws Exception {

    }

    @Test
    public void testGetSuperClassGenricType() throws Exception {

    }
}