package com.fantasy.framework.lucene.mapper;

public class MapperUtil {

	public static String getEntityName(Class<?> clazz) {
		// Entity entity = (Entity) clazz.getAnnotation(Entity.class);
		String name = "";// entity.name();
		if (name.equals("")) {
			name = clazz.getName().toLowerCase();
		}
		return name;
	}

}