package com.fantasy.swp.data;

import com.fantasy.swp.PageData;

/**
 * 简单变量接口
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2013-7-15 下午05:22:54
 * @version 1.0
 */
public class SimpleData extends AbstractPageData {

	private Object value;

	public static PageData getData(String key, Object value) {
		SimpleData data = new SimpleData();
		data.key = key;
		data.value = value;
		return data;
	}

	public Object getValue() {
		return value;
	}

}
