package com.fantasy.swp.data;

import com.fantasy.swp.PageData;

public abstract class AbstractPageData implements PageData {
	protected String key;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
