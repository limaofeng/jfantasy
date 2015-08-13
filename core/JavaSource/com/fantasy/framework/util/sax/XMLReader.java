package com.fantasy.framework.util.sax;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;

public class XMLReader {
    private XMLReader() {
    }

    private static final Log LOGGER = LogFactory.getLog(XMLReader.class);

    public static XmlElement reader(String path) {
        File xmlfile = new File(path);
        LOGGER.info("开始解析XML文件：[" + path + "]");
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setValidating(false);
        SAXParser saxParse = null;
        try {
            saxParse = factory.newSAXParser();
            SaxXmlHandler handler = new SaxXmlHandler();
            saxParse.parse(xmlfile, handler);
            LOGGER.info("XML文件：[" + path + "]解析完成");
            return handler.getElement();
        } catch (ParserConfigurationException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (SAXException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    public static XmlElement reader(String path, InputStream stream) {
        LOGGER.info("开始解析XML文件：[" + path + "]");
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setValidating(false);
        SAXParser saxParse = null;
        try {
            saxParse = factory.newSAXParser();
            SaxXmlHandler handler = new SaxXmlHandler();
            saxParse.parse(stream, handler);
            LOGGER.info("XML文件：[" + path + "]解析完成");
            return handler.getElement();
        } catch (ParserConfigurationException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (SAXException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    public static String toJSON(XmlElement element) {
        String retVal = parse(element);
        LOGGER.info("将XML文件转为JSON：" + retVal);
        return retVal;
    }

    private static String parse(XmlElement element) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("{").append(element.getTagName()).append(":").append(attributeToString(element)).append("}");
        return buffer.toString();
    }

    private static String attributeToString(XmlElement element) {
        StringBuffer buffer = new StringBuffer();
        Map<String, String> attributes = element.getAttribute();
        Iterator<String> iterator = attributes.keySet().iterator();
        buffer.append("{");
        while (iterator.hasNext()) {
            String key = iterator.next();
            buffer.append(key).append(":").append("\"").append(attributes.get(key)).append("\"").append(iterator.hasNext() ? "," : "");
        }
        if ((element.getChildNodes() != null) && (!element.getChildNodes().isEmpty())) {
            buffer.append(buffer.length() > 1 ? "," : "").append(subElementsToString(element));
        }
        buffer.append("}");
        return buffer.toString();
    }

    private static String subElementsToString(XmlElement element) {
        StringBuffer buffer = new StringBuffer();
        List<XmlElement> list = element.getChildNodes();
        if (list != null) {
            Map<String, List<XmlElement>> map = new HashMap<String, List<XmlElement>>();
            for (int i = 0; i < list.size(); i++) {
                XmlElement ele = list.get(i);
                if (!map.containsKey(ele.getTagName())) {
                    map.put(ele.getTagName(), new ArrayList<XmlElement>());
                }
                List<XmlElement> nList = map.get(ele.getTagName());
                nList.add(ele);
            }
            Iterator<String> iterator = map.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                buffer.append(key).append(":").append("[");
                List<XmlElement> nList = map.get(key);
                for (int i = 0; i < nList.size(); i++) {
                    buffer.append(i == 0 ? "" : ",").append(attributeToString((XmlElement) nList.get(i)));
                }
                buffer.append("]").append(iterator.hasNext() ? "," : "");
            }
        }
        return buffer.toString();
    }
}