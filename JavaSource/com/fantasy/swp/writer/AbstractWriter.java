package com.fantasy.swp.writer;

import com.fantasy.file.FileManager;
import com.fantasy.swp.PageUrl;
import com.fantasy.swp.PageWriter;

public abstract class AbstractWriter implements PageWriter {

	protected FileManager fileManager;
	protected PageUrl pageUrl;

	public void setFileManager(FileManager fileManager) {
		this.fileManager = fileManager;
	}

	public void setPageUrl(PageUrl pageUrl) {
		this.pageUrl = pageUrl;
	}

}
