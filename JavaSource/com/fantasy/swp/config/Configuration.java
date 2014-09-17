package com.fantasy.swp.config;

import com.fantasy.swp.config.entities.PageConfig;

/**
 * 配置类
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2013-3-29 上午12:30:47
 * @version 1.0
 */
public interface Configuration {

	/**
	 * 添加Page配置
	 * 
	 * @功能描述
	 * @param name
	 * @param pageConfig
	 */
	void addPageConfig(String name, PageConfig pageConfig);

	/**
	 * 移除Page配置
	 * 
	 * @功能描述
	 * @param pageName
	 * @return
	 */
	PageConfig removePageConfig(String pageName);
}
