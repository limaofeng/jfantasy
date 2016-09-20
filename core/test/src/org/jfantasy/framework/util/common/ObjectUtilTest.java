package org.jfantasy.framework.util.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ObjectUtilTest {

    private final static Log LOG = LogFactory.getLog(ObjectUtilTest.class);

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testClone() throws Exception {

    }

    @Test
    public void testToString() throws Exception {

    }

    @Test
    public void testFilter() throws Exception {
        List<ObjectTestBean> list = new ArrayList<>();
        list.add(new ObjectTestBean("limaofeng",Boolean.TRUE,32));
        list.add(new ObjectTestBean("wangmingliang",Boolean.TRUE,27));
        list.add(new ObjectTestBean("duanxiangbing",Boolean.FALSE,25));

        List<ObjectTestBean> listf = ObjectUtil.filter(list,"locked",Boolean.TRUE);

        Assert.assertEquals(listf.size(),2);
    }

    @Test
    public void testToFieldArray() throws Exception {

    }

    @Test
    public void testGetMinObject() throws Exception {

    }

    @Test
    public void testIndexOf() throws Exception {

    }

    @Test
    public void testSort() throws Exception {

    }

    @Test
    public void testAnalyze() throws Exception {
        LOG.debug(ObjectUtil.analyze("上海昊略公司，提供应用软件和服务"));
    }

    public static class ObjectTestBean{
        private String name;
        private Boolean locked;
        private int age;

        public ObjectTestBean(String name, Boolean locked, int age) {
            this.name = name;
            this.locked = locked;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public Boolean getLocked() {
            return locked;
        }

        public int getAge() {
            return age;
        }
    }
}