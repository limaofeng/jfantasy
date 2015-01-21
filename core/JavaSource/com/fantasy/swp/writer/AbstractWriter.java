package com.fantasy.swp.writer;

import com.fantasy.file.FileManager;
import com.fantasy.swp.OutPutUrl;
import com.fantasy.swp.PageInstance;
import com.fantasy.swp.PageService;
import com.fantasy.swp.TemplateData;

public abstract class AbstractWriter implements PageService {

	protected FileManager fileManager;
	protected OutPutUrl pageUrl;

	public void setFileManager(FileManager fileManager) {
		this.fileManager = fileManager;
	}

	public void setPageUrl(OutPutUrl pageUrl) {
		this.pageUrl = pageUrl;
	}
	@Override
	public PageInstance createPageInstance(OutPutUrl outPutUrl, com.fantasy.swp.Template template, TemplateData... datas) {
		return null;
	}
}
