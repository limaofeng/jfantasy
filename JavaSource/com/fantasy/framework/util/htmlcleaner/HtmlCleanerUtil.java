package com.fantasy.framework.util.htmlcleaner;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlcleaner.BrowserCompactXmlSerializer;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.htmlcleaner.XmlSerializer;

/**
 * @author 李茂峰
 * @version 1.0
 * @功能描述 HtmlCleaner是一个开源的Java语言的Html文档解析器。HtmlCleaner能够重新整理HTML文档的每个元素并生成结构良好(Well-Formed)的 HTML 文档。
 * @since 2013-8-13 下午01:41:42
 */
public class HtmlCleanerUtil {

    private static HtmlCleaner hc = new HtmlCleaner();

    private static Log logger = LogFactory.getLog(HtmlCleanerUtil.class);

    public static void main(String[] args) throws Exception {
        CleanerProperties props = hc.getProperties();//new CleanerProperties();
        System.out.println(props.isUseCdataForScriptAndStyle());
//		props.setUseCdataForScriptAndStyle(true);
        System.out.println(props.isRecognizeUnicodeChars());
//		props.setRecognizeUnicodeChars(true);
        System.out.println(props.isUseEmptyElementTags());
//		props.setUseEmptyElementTags(true);
        System.out.println(props.isAdvancedXmlEscape());
//		props.setAdvancedXmlEscape(true);
        System.out.println(props.isTranslateSpecialEntities());
//		props.setTranslateSpecialEntities(true);
        System.out.println(props.getBooleanAttributeValues());
//		props.setBooleanAttributeValues("empty");

        TagNode node = HtmlCleanerUtil.htmlCleaner("<Bean><a>xxx</a></Bean>");

        System.out.println(HtmlCleanerUtil.findFristTagNode(node, "//bean//a").getText());

//		TagNode node = htmlCleaner(new URL("http://www.baidu.com/"));
//		
//		for(TagNode tagNode : findTagNodes(node, "//a")){
//			System.out.println(tagNode);
//			tagNode.setAttribute("href", "xxxx");
//			String html = getAsString(getBrowserCompactXmlSerializer(hc), tagNode);
//			System.out.println(html);
//		}
//		
//		String html = getAsString(getBrowserCompactXmlSerializer(hc), node);
//		System.out.println(html);

    }

    public static TagNode[] findTagNodes(String html, String xpath) {
        return findTagNodes(htmlCleaner(html), xpath);
    }

    public static TagNode[] findTagNodes(Reader reader, String xpath) {
        try {
            return findTagNodes(htmlCleaner(reader), xpath);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return new TagNode[0];
    }

    public static TagNode[] findTagNodes(InputStream in, String xpath) {
        try {
            return findTagNodes(htmlCleaner(in), xpath);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return new TagNode[0];
    }

    public static TagNode[] findTagNodes(InputStream in, String charset, String xpath) {
        try {
            return findTagNodes(htmlCleaner(in, charset), xpath);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return new TagNode[0];
    }

    public static TagNode[] findTagNodes(URL url, String xpath) {
        try {
            return findTagNodes(htmlCleaner(url), xpath);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return new TagNode[0];
    }

    public static TagNode[] findTagNodes(URL url, String charset, String xpath) {
        try {
            return findTagNodes(htmlCleaner(url, charset), xpath);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return new TagNode[0];
    }

    public static TagNode[] findTagNodes(File file, String xpath) {
        try {
            return findTagNodes(htmlCleaner(file), xpath);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return new TagNode[0];
    }

    public static TagNode[] findTagNodes(File file, String charset, String xpath) {
        try {
            return findTagNodes(htmlCleaner(file, charset), xpath);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return new TagNode[0];
    }

    public static TagNode[] findTagNodes(TagNode node, String xpath) {
        try {
            Object[] objs = node.evaluateXPath(xpath);
            TagNode[] nodes = new TagNode[objs.length];
            for (int i = 0; i < objs.length; i++) {
                nodes[i] = (TagNode) objs[i];
            }
            return nodes;
        } catch (XPatherException e) {
            logger.error(e.getMessage(), e);
        }
        return new TagNode[0];
    }

    public static TagNode findFristTagNode(TagNode node, String xpath) {
        try {
            Object[] objects = node.evaluateXPath(xpath);
            if (objects.length > 0) {
                return (TagNode) objects[0];
            }
        } catch (XPatherException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public static TagNode findFristTagNode(URL url, String xpath) {
        try {
            return findFristTagNode(htmlCleaner(url), xpath);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public static TagNode findFristTagNode(URL url, String charset, String xpath) {
        try {
            return findFristTagNode(htmlCleaner(url, charset), xpath);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public static TagNode findFristTagNode(String html, String xpath) {
        return findFristTagNode(htmlCleaner(html), xpath);
    }

    public static TagNode htmlCleaner(File file) throws IOException {
        return hc.clean(file);
    }

    public static TagNode htmlCleaner(File file, String charset) throws IOException {
        return hc.clean(file, charset);
    }

    public static TagNode htmlCleaner(URL url) throws IOException {
        return hc.clean(url);
    }

    public static TagNode htmlCleaner(URL url, String charset) throws IOException {
        return hc.clean(url, charset);
    }

    public static TagNode htmlCleaner(InputStream in) throws IOException {
        return hc.clean(in);
    }

    public static TagNode htmlCleaner(InputStream in, String charset) throws IOException {
        return hc.clean(in, charset);
    }

    public static TagNode htmlCleaner(Reader reader) throws IOException {
        return hc.clean(reader);
    }

    public static TagNode htmlCleaner(String html) {
        return hc.clean(html);
    }

    public static String findByAttValue(String htmlPath, String att, String value) {
        try {
            HtmlCleaner hc = new HtmlCleaner();
            TagNode node = hc.clean(new File(htmlPath), "UTF-8");
            TagNode tagNode = node.findElementByAttValue(att, value, true, true);
            return getAsString(getBrowserCompactXmlSerializer(hc), tagNode);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String[] evaluateXPath(TagNode node, String xpath) {
        TagNode[] nodes = findTagNodes(node, xpath);
        String[] htmls = new String[nodes.length];
        for (int i = 0, len = nodes.length; i < len; i++) {
            htmls[i] = getAsString(getBrowserCompactXmlSerializer(hc), nodes[i]);
        }
        return htmls;
    }

    public static XmlSerializer getBrowserCompactXmlSerializer(HtmlCleaner hc) {
        return new BrowserCompactXmlSerializer(hc.getProperties());
    }

    public static String getAsString(XmlSerializer xmlSerializer, TagNode tagNode) {
        return xmlSerializer.getAsString(tagNode);
    }

    public static String getAsString(TagNode tagNode) {
        return getBrowserCompactXmlSerializer(hc).getAsString(tagNode);
    }
}