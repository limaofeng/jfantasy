package com.fantasy.swp.config.impl;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fantasy.swp.config.Configuration;
import com.fantasy.swp.config.entities.PageConfig;

public class DefaultConfiguration implements Configuration{

	protected static final Log logger = LogFactory.getLog(DefaultConfiguration.class);
	
	protected Map<String, PageConfig> pageContexts = new LinkedHashMap<String, PageConfig>();
	
	
    public Set<String> getPageConfigNames() {
        return pageContexts.keySet();
    }

    public Map<String, PageConfig> getPackageConfigs() {
        return pageContexts;
    }

	public void addPageConfig(String name, PageConfig pageConfig) {
		
	}

	public PageConfig removePageConfig(String pageName) {
		return null;
	}

}
