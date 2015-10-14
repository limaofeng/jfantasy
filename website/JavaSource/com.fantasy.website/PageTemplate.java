package com.fantasy.website;

import freemarker.template.Template;

import java.io.OutputStream;
import java.util.List;

/**
 * 模板页
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2013-9-13 上午11:08:31
 * @version 1.0
 */
public class PageTemplate {
	// 添加模板步骤
	// 1.模板对应的文件管理器
	private Template template;
	// 3.配置数据 如果数据之间有依赖的话。需要配置监听功能。
	private List<TemplateData> datas;
	// 4.配置预览数据
	private List<TemplateData> previewDatas;
	// 5.文件生成地址生成器
	private String manager;

	// 6.配置模板生成时对应的文件管理器. 一般指向apache对应的目录
	// 7.完成

	/**
	 * 预览模板
	 * 
	 * @功能描述
	 * @param out
	 */
	public void preview(OutputStream out) {

	}

	/**
	 * 生成页面
	 * 
	 * @功能描述
	 */
	public void execute() {

	}

}
