package com.fantasy.swp;

import java.io.IOException;

import com.fantasy.file.FileManager;
import com.fantasy.swp.util.DataMap;
import com.fantasy.swp.PageUrl;

import freemarker.template.Template;

/**
 * 页面生成接口
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2013-3-28 下午01:31:54
 * @version 1.0
 */
public interface PageWriter {

	/**
	 * 生成页面
	 * 
	 * @功能描述
	 * @param template
	 *            FTL模板对象
	 * @param data
	 *            模板数据
	 * @param pageUrl
	 *            生成的地址
	 * @throws IOException
	 */
	public void execute(Template template, DataMap data) throws IOException;

	public void setFileManager(FileManager fileManager);

	public void setPageUrl(PageUrl pageUrl);

}
