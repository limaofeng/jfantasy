package com.fantasy.swp.url;

import java.util.Map;

public class SimpleUrl extends AbstractOutPutUrl {

	private String url;

	public SimpleUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	@Override
	public String getUrl(Map<String, Object> data) {
		return this.getUrl();
	}
}
