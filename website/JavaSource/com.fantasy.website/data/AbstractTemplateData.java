package org.jfantasy.website.data;

import org.jfantasy.website.TemplateData;

public abstract class AbstractTemplateData implements TemplateData {
	protected String key;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
