package org.jfantasy.website.data;

/**
 * 简单变量接口
 * 
 * @author 李茂峰
 * @since 2013-7-15 下午05:22:54
 * @version 1.0
 */
public class SimpleData extends AbstractTemplateData {

	private Object value;

	public SimpleData(Object value) {
		this.value = value;
	}

	public Object getValue() {
		return value;
	}

}
