package com.fantasy.swp.backend;


public class EntityChangedListener {
	private Class<?> clazz;

	protected EntityChangedListener(Class<?> clazz) {
		this.clazz = clazz;
	}


	public void entityInsert(Object entity) {

	}

	public void entityDelete(Object entity) {

	}

	public void entityUpdate(Object entity) {

	}

}
