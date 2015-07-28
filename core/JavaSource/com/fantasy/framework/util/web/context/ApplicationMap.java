package com.fantasy.framework.util.web.context;

import java.util.AbstractMap;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;

@SuppressWarnings("rawtypes")
public class ApplicationMap extends AbstractMap {

	private ServletContext context;
	private Set<Object> entries;

	public ApplicationMap(ServletContext ctx) {
		this.context = ctx;
	}

	public void clear() {
		entries = null;
		Enumeration e = context.getAttributeNames();
		while (e.hasMoreElements()) {
			context.removeAttribute(e.nextElement().toString());
		}
	}

	public Set entrySet() {
		if (entries == null) {
			entries = new HashSet<Object>();
			Enumeration enumeration = context.getAttributeNames();
			while (enumeration.hasMoreElements()) {
				final String key = enumeration.nextElement().toString();
				final Object value = context.getAttribute(key);
				entries.add(new Map.Entry() {
					public boolean equals(Object obj) {
						if (!(obj instanceof Map.Entry)) {
							return false;
						}
						Map.Entry entry = (Map.Entry) obj;
						return key == null ? (entry.getKey() == null) : key.equals(entry.getKey()) && value == null ? (entry.getValue() == null) : value.equals(entry.getValue());
					}

					public int hashCode() {
						return ((key == null) ? 0 : key.hashCode()) ^ ((value == null) ? 0 : value.hashCode());
					}

					public Object getKey() {
						return key;
					}

					public Object getValue() {
						return value;
					}

					public Object setValue(Object obj) {
						context.setAttribute(key, obj);
						return value;
					}
				});
			}
			enumeration = context.getInitParameterNames();
			while (enumeration.hasMoreElements()) {
				final String key = enumeration.nextElement().toString();
				final Object value = context.getInitParameter(key);
				entries.add(new Map.Entry() {
					public boolean equals(Object obj) {
						if (!(obj instanceof Map.Entry)) {
							return false;
						}
						Map.Entry entry = (Map.Entry) obj;

						return (key == null) ? (entry.getKey() == null) : key.equals(entry.getKey()) && (value == null) ? (entry.getValue() == null) : value.equals(entry.getValue());
					}

					public int hashCode() {
						return ((key == null) ? 0 : key.hashCode()) ^ ((value == null) ? 0 : value.hashCode());
					}

					public Object getKey() {
						return key;
					}

					public Object getValue() {
						return value;
					}

					public Object setValue(Object obj) {
						context.setAttribute(key, obj);
						return value;
					}
				});
			}
		}
		return entries;
	}

	public Object get(Object key) {
		String keyString = key.toString();
		Object value = context.getAttribute(keyString);
		return (value == null) ? context.getInitParameter(keyString) : value;
	}

	public Object put(Object key, Object value) {
		Object oldValue = get(key);
		entries = null;
		context.setAttribute(key.toString(), value);
		return oldValue;
	}

	public Object remove(Object key) {
		entries = null;
		Object value = get(key);
		context.removeAttribute(key.toString());
		return value;
	}
}
