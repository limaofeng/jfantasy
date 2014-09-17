package com.fantasy.framework.util.cglib.interceptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fantasy.framework.util.regexp.RegexpUtil;

public class ParameterMap extends HashMap<String, Object> {

	private static final long serialVersionUID = 6842517213822127863L;

	public boolean containsKey(Object key) {
		return super.containsKey(key);
	}

	@SuppressWarnings("unchecked")
	public Object put(String key, Object value) {
		if ((value instanceof String)) {
			if (isArray(key)) {
				String propertyName = getPropertyName(key);
				String name = RegexpUtil.replaceFirst(key, "\\[.*?\\]", "");
				if (!super.containsKey(propertyName)) {
					List<Object> array = new ArrayList<Object>();
					super.put(propertyName, array);
				}
				String seq = RegexpUtil.parseGroup(key, "^" + propertyName + "\\[(\\d+)\\]", 1);
				if (isBean(name)) {
					List<Object> array = (List<Object>) super.get(propertyName);
					int index = Integer.valueOf(seq).intValue();
					if (array.size() <= index) {
						for (int i = array.size(); i <= index; i++) {
							array.add(array.get(i));
						}
					}
					if (array.get(index) == null)
						array.set(index, new ParameterMap());
					((ParameterMap) array.get(index)).put(RegexpUtil.replace(name, "^" + propertyName + ".", ""), value);
				} else {
					List<Object> array = (List<Object>) super.get(propertyName);
					int index = Integer.valueOf(seq).intValue();
					if (array.size() <= index) {
						for (int i = array.size(); i <= index; i++) {
							array.add(array.get(i));
						}
					}
					array.set(index, value);
				}
			} else if (isBean(key)) {
				String propertyName = getPropertyName(key);
				if (!super.containsKey(propertyName)) {
					super.put(propertyName, new ParameterMap());
				}
				((ParameterMap) super.get(propertyName)).put(RegexpUtil.replace(key, "^" + propertyName + ".", ""), value);
			}
		}
		return super.put(key, value);
	}

	public Set<Map.Entry<String, Object>> entrySet() {
		return super.entrySet();
	}

	public Set<String> keySet() {
		return super.keySet();
	}

	private static String getPropertyName(String param) {
		String returnVal = param;
		if (isBean(param)) {
			String[] fieldNames = param.split("\\.");
			return getPropertyName(fieldNames[0]);
		}
		String regEx = "(.*?)\\[\\d\\]$";
		String group = RegexpUtil.parseGroup(param, regEx, 1);
		if (group != null) {
			returnVal = group;
		}
		return returnVal;
	}

	private static boolean isBean(String fieldName) {
		if (fieldName.indexOf(".") <= -1)
			if (!RegexpUtil.find(fieldName, new String[] { "^[0-9]+$" }))
				return false;
		return true;
	}

	private static boolean isArray(String fieldName) {
		if (isBean(fieldName)) {
			fieldName = fieldName.split("\\.")[0];
		}
		String regEx = "\\[\\d+\\]$";
		return RegexpUtil.isMatch(fieldName, regEx);
	}
}