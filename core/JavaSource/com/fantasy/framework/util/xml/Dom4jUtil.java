package com.fantasy.framework.util.xml;

import com.fantasy.framework.error.IgnoreException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.*;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

public final class Dom4jUtil {

	private static final Log logger = LogFactory.getLog(Dom4jUtil.class);

	public static Document reader(InputStream inputStream) {
		try {
			return new SAXReader().read(inputStream);
		} catch (Exception e) {
			logger.error(e);
			throw new IgnoreException(e.getMessage());
		}
	}

	public static Document reader(URL url) {
		try {
			return new SAXReader().read(url);
		} catch (Exception e) {
			logger.error(e);
			throw new IgnoreException(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public static void readNode(Element root, String prefix) {
		if (root == null){
            return;
        }
		// 获取属性
		List<Attribute> attrs = root.attributes();
		if (attrs != null && attrs.size() > 0) {
			logger.error(prefix);
			for (Attribute attr : attrs) {
				logger.error(attr.getValue() + " ");
			}
		}
		// 获取他的子节点
		List<Element> childNodes = root.elements();
		prefix += "\t";
		for (Element e : childNodes) {
			readNode(e, prefix);
		}
	}

	public static class MyVistor extends VisitorSupport {
		public void visit(Attribute node) {
			logger.debug("Attibute: " + node.getName() + "=" + node.getValue());
		}

		public void visit(Element node) {
			if (node.isTextOnly()) {
				logger.debug("Element: " + node.getName() + "=" + node.getText());
			} else {
				logger.debug("root:"+node.getName());
			}
		}

		@Override
		public void visit(ProcessingInstruction node) {
			logger.debug("PI:" + node.getTarget() + " " + node.getText());
		}
	}

}
