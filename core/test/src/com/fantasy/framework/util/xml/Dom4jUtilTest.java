package org.jfantasy.framework.util.xml;

import org.dom4j.Document;
import org.junit.Before;
import org.junit.Test;

public class Dom4jUtilTest {

    @Before
    public void setUp() throws Exception {
        Document document = Dom4jUtil.reader(Dom4jUtil.class.getResourceAsStream("test.xml"));
        document.accept(new Dom4jUtil.MyVistor()) ;
    }

    @Test
    public void testReader() throws Exception {

    }

    @Test
    public void testReadNode() throws Exception {

    }
}