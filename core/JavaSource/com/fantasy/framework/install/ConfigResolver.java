package com.fantasy.framework.install;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;

public class ConfigResolver {

    private ConfigResolver() {
    }

    private final static Log LOG = LogFactory.getLog(ConfigResolver.class);

    private static Configuration configuration;

    private final static ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

    private static DocumentBuilder builder;

    static {
        try {
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    public static Configuration parseConfiguration() throws IOException {
        if (configuration != null) {
            return configuration;
        }
        long time = System.currentTimeMillis();
        configuration = new Configuration();
        Resource[] resources = resourcePatternResolver.getResources("classpath*:/install.xml");
        for (Resource resource : resources) {
            LOG.debug("加载:" + resource);
            try {
                Document document = builder.parse(resource.getInputStream());
                // 生成XPath对象
                XPath xpath = XPathFactory.newInstance().newXPath();
                //解析 hibernate packagesToScan
                NodeList packagesToScanNodes = (NodeList) xpath.evaluate("//fantasy//hibernate//sessionFactory//packagesToScan//list//value", document, XPathConstants.NODESET);
                for (int i = 0; i < packagesToScanNodes.getLength(); i++) {
                    configuration.addPackagesToScan(packagesToScanNodes.item(i).getTextContent());
                }
            } catch (SAXException e) {
                LOG.error(e.getMessage(), e);
            } catch (XPathExpressionException e) {
                LOG.error(e.getMessage(), e);
            }
        }
        LOG.debug(" 解析 install.xml 耗时 " + (System.currentTimeMillis() - time) + "ms");
        return configuration;
    }

}
