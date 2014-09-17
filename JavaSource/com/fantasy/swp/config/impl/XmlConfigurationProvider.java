package com.fantasy.swp.config.impl;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;

import com.fantasy.swp.config.Configuration;
import com.fantasy.swp.config.ConfigurationProvider;
import com.opensymphony.xwork2.util.ClassLoaderUtil;

/**
 * 从XML加载配置
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2013-3-29 上午12:30:26
 * @version 1.0
 */
public class XmlConfigurationProvider implements ConfigurationProvider {

	protected static final Log logger = LogFactory.getLog(XmlConfigurationProvider.class);
	
	private List<Document> documents;
	private Configuration configuration;
	private String configFileName;

	public XmlConfigurationProvider() {
		this("webpage.xml");
	}

	public XmlConfigurationProvider(String filename) {
		this.configFileName = filename;
	}

	public void init(Configuration configuration) {
		this.configuration = configuration;
		loadDocuments(configFileName);
	}

	private void loadDocuments(String configFileName) {
		documents = loadConfigurationFiles(configFileName);
	}

	private List<Document> loadConfigurationFiles(String configFileName) {
		try {
			Iterator<URL> iterator = getConfigurationUrls(configFileName);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public void loadPages() {

	}

	protected Iterator<URL> getConfigurationUrls(String fileName) throws IOException {
		return ClassLoaderUtil.getResources(fileName, XmlConfigurationProvider.class, false);
	}

}
