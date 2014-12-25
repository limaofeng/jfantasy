package com.fantasy.framework.util.xml;

import org.dom4j.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class Dom4jUtilTest {

    @Before
    public void setUp() throws Exception {
        Document document = Dom4jUtil.reader(Dom4jUtil.class.getResourceAsStream("test.xml"));
        document.accept(new Dom4jUtil.MyVistor()) ;
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testReader() throws Exception {

    }

    @Test
    public void testReader1() throws Exception {

    }

    @Test
    public void testReadNode() throws Exception {

    }
}