package com.fantasy.swp.config;

import com.fantasy.swp.config.impl.DefaultConfiguration;

/**
 * 配置的管理类
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2013-3-29 上午12:31:06
 * @version 1.0
 */
public class ConfigurationManager {

	protected static Configuration configuration;

	public static Configuration getConfiguration() {
		if (configuration == null) {
			setConfiguration(createConfiguration());
		}
		return configuration;
	}

	private static Configuration createConfiguration() {
		return new DefaultConfiguration();
	}

	public synchronized static void setConfiguration(Configuration configuration) {
		ConfigurationManager.configuration = configuration;
	}

}
