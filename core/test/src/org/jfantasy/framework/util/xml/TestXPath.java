package org.jfantasy.framework.util.xml;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.IOException;
import java.io.InputStream;

public class TestXPath {

    private static Log LOG = LogFactory.getLog(TestXPath.class);

    @Test
    public void read() {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbf.newDocumentBuilder();
            InputStream in = TestXPath.class.getResourceAsStream("test.xml");
            Document doc = builder.parse(in);
            XPathFactory factory = XPathFactory.newInstance();
            XPath xpath = factory.newXPath();
            // 选取所有class元素的name属性
            // XPath语法介绍： http://w3school.com.cn/xpath/
            XPathExpression expr = xpath.compile("//class/@name");
            NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            for (int i = 0; i < nodes.getLength(); i++) {
                LOG.debug("name = " + nodes.item(i).getNodeValue());
            }
        } catch (XPathExpressionException e) {
            LOG.debug(e.getMessage(), e);
        } catch (ParserConfigurationException e) {
            LOG.debug(e.getMessage(), e);
        } catch (SAXException e) {
            LOG.debug(e.getMessage(), e);
        } catch (IOException e) {
            LOG.debug(e.getMessage(), e);
        }
    }

}
