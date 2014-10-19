package com.fantasy.swp.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.fantasy.swp.PageData;

public class DataMap implements Map<String, Object> {

	private Map<String, PageData> pageDatas = new HashMap<String, PageData>();

	public boolean containsKey(java.lang.Object key) {
		return this.pageDatas.containsKey(key);
	}

	public boolean containsValue(java.lang.Object value) {
		throw new RuntimeException("对象不支持该方法!");
	}

	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		throw new RuntimeException("对象不支持该方法!");
	}

	public Object get(java.lang.Object key) {
		return this.pageDatas.get(key).getValue();
	}

	public boolean isEmpty() {
		return this.pageDatas.isEmpty();
	}

	public Set<String> keySet() {
		return this.pageDatas.keySet();
	}

	public void add(PageData data) {
		this.pageDatas.put(data.getKey(), data);
	}

	public Object put(String key, Object value) {
		throw new RuntimeException("对象不支持该方法!");
	}

	public void putAll(Map<? extends String, ? extends Object> t) {
		throw new RuntimeException("对象不支持该方法!");
	}

	public Object remove(java.lang.Object key) {
		return this.pageDatas.remove(key);
	}

	public int size() {
		return this.pageDatas.size();
	}

	public Collection<Object> values() {
		throw new RuntimeException("对象不支持该方法!");
	}

	public void clear() {
		this.pageDatas.clear();
	}

}
