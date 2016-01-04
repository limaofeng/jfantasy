package org.jfantasy.framework.util.htmlcleaner;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlcleaner.TagNode;
import org.junit.Test;

public class HtmlCleanerUtilTest {

    private final static Log logger = LogFactory.getLog(HtmlCleanerUtilTest.class);

    @Test
    public void testFindTagNodes() throws Exception {

    }

    @Test
    public void testFindFristTagNode() throws Exception {

    }

    @Test
    public void testHtmlCleaner() throws Exception {

    }

    @Test
    public void testFindByAttValue() throws Exception {

    }

    @Test
    public void testEvaluateXPath() throws Exception {

    }

    @Test
    public void testGetBrowserCompactXmlSerializer() throws Exception {

    }

    @Test
    public void testGetAsString() throws Exception {
        TagNode root = HtmlCleanerUtil.htmlCleaner(HtmlCleanerUtilTest.class.getResource("article.xml"),"utf-8");
        TagNode text = HtmlCleanerUtil.findFristTagNode(root,"//div[@class='feed-text']");
        logger.debug(HtmlCleanerUtil.getAsString(text));
    }
}