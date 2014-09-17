package com.fantasy.swp.url;

import java.util.Map;

import com.fantasy.swp.PageUrl;

public class SimpleUrl implements PageUrl {

	private String url;

	public SimpleUrl() {
	}
	
	public SimpleUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl(Map<String, Object> data) {
		return getUrl();
	}

}
