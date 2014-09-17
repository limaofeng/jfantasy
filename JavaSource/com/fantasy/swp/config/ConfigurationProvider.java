package com.fantasy.swp.config;

/**
 * 配置加载类
 * 
 * @功能描述 <br/>
 *       以后会提供从数据库加载的功能
 * @author 李茂峰
 * @since 2013-3-29 上午12:31:27
 * @version 1.0
 */
public interface ConfigurationProvider {

	/**
	 * 初始化方法
	 * 
	 * @功能描述
	 * @param configuration
	 */
	public void init(Configuration configuration);

	/**
	 * 加载所有的PageConfig
	 * 
	 * @功能描述
	 */
	public void loadPages();

}
