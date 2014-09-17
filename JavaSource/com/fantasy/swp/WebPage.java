package com.fantasy.swp;

import java.io.IOException;

import com.fantasy.swp.util.DataMap;
import com.fantasy.swp.PageData;
import com.fantasy.swp.PageWriter;

import freemarker.template.Configuration;

public class WebPage {

	private String name;
	
	private String template;
	
	private Configuration freemarkerManager;
	
	private PageWriter pageWriter;

	private DataMap globalDatas = new DataMap();

	private DataMap data = new DataMap();

	public void process(){
		try {
			this.pageWriter.execute(freemarkerManager.getTemplate(this.template), this.data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setGlobalDatas(DataMap globalDatas) {
		this.globalDatas = globalDatas;
	}

	public void addData(PageData data) {
		this.data.add(data);
	}

	public PageWriter getPageWriter() {
		return pageWriter;
	}

	public void setPageWriter(PageWriter pageWriter) {
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
