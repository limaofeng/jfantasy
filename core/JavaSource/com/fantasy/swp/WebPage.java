package com.fantasy.swp;

import com.fantasy.swp.util.DataMap;
import freemarker.template.Configuration;
import org.apache.log4j.Logger;

public class WebPage {

	private static final Logger LOG = Logger.getLogger(WebPage.class);

	private String name;
	
	private String template;
	
	private Configuration freemarkerManager;
	
	private PageService pageWriter;

	private DataMap globalDatas = new DataMap();

	private DataMap data = new DataMap();

//	public void process(){
//		try {
//			this.pageWriter.execute(freemarkerManager.getTemplate(this.template), this.data);
//		} catch (IOException e) {
//			LOG.error(e.getMessage(),e);
//		}
//	}
//
	public void setGlobalDatas(DataMap globalDatas) {
		this.globalDatas = globalDatas;
	}

	public void addData(TemplateData data) {
		this.data.add(data);
	}

	public PageService getPageWriter() {
		return pageWriter;
	}

	public void setPageWriter(PageService pageWriter) {
		this.pageWriter = pageWriter;
	}

	public DataMap getData() {
		return data;
	}

	public void setData(DataMap data) {
		this.data = data;
	}

	public DataMap getGlobalDatas() {
		return globalDatas;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public Configuration getFreemarkerManager() {
		return freemarkerManager;
	}

	public void setFreemarkerManager(Configuration freemarkerManager) {
		this.freemarkerManager = freemarkerManager;
	}

}
