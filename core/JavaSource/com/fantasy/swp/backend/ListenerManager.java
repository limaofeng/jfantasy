package com.fantasy.swp.backend;

import java.util.HashMap;
import java.util.Map;

public class ListenerManager {

	private static Map<Class<?>, EntityChangedListener> entityChangeds = new HashMap<Class<?>, EntityChangedListener>();

	public static EntityChangedListener getInstance(Class<?> clazz) {
		if (!entityChangeds.containsKey(clazz)) {
			entityChangeds.put(clazz, new EntityChangedListener(clazz));
		}
		return entityChangeds.get(clazz);
	}
	
}
