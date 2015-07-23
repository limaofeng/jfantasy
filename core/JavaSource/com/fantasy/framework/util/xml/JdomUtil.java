package com.fantasy.framework.util.xml;

import com.fantasy.framework.error.IgnoreException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class JdomUtil {
	private static final Log LOGGER = LogFactory.getLog(JdomUtil.class);

	public static Document reader(String filePath) {
		return reader(new File(filePath));
	}

	public static Document reader(File file) {
		try {
			return reader(new FileInputStream(file));
		} catch (Exception e) {
			LOGGER.error(e);
			throw new IgnoreException(e.getMessage());
		}
	}

	public static Document reader(InputStream inputStream) {
		try {
			return new SAXBuilder().build(inputStream);
		} catch (Exception e) {
			LOGGER.error(e);
			throw new IgnoreException(e.getMessage());
		}
	}

	public static Document reader(URL url) {
		try {
			return new SAXBuilder().build(url);
		} catch (Exception e) {
			LOGGER.error(e);
			throw new IgnoreException(e.getMessage());
		}
	}

	public static Element parse(InputStream inputStream, Parser parser) {
		Document doc = reader(inputStream);
		Element root = doc.getRootElement();
		each(root, parser);
		return root;
	}

	public static void parse(String filePath, Parser parser) {
		Document doc = reader(filePath);
		Element root = doc.getRootElement();
		each(root, parser);
	}

	@SuppressWarnings("unchecked")
	private static void each(Element ele, Parser parser) {
		for (Element element : (List<Element>) ele.getChildren()) {
			parser.callBack(element.getName(), element);
			each(element, parser);
		}
	}

	public static abstract interface Parser {
		public abstract void callBack(String paramString, Element paramElement);
	}
}