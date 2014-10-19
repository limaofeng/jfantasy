package com.fantasy.framework.service;

import java.io.IOException;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import com.fantasy.file.FileManager;
import com.fantasy.file.manager.LocalFileManager;

/**
 * 将远程文件写到本地磁盘
 * 
 * @功能描述
 * @状态 开发中...
 * @author 李茂峰
 * @since 2013-6-4 下午04:10:19
 * @version 1.0
 */
public class LocalizationService implements InitializingBean {

	private FileManager fileManager;

	private FileManager origFileManager;

	public void afterPropertiesSet() throws Exception {
		Assert.notNull(this.fileManager, "Property 'fileManager' is required");
		Assert.isTrue(this.fileManager instanceof LocalFileManager, "Property 'fileManager' not typeOf LocalFileManager");
		Assert.notNull(this.origFileManager, "Property 'origFileManager' is required");
	}

	public void localization(String filepath) throws IOException {
		fileManager.writeFile(filepath, origFileManager.readFile(filepath));
	}

	public void setFileManager(FileManager fileManager) {
		this.fileManager = fileManager;
	}

	public void setOrigFileManager(FileManager origFileManager) {
		this.origFileManager = origFileManager;
	}

}
