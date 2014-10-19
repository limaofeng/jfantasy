package com.fantasy.framework.service;

import java.util.List;

import com.fantasy.framework.util.jackson.JSON;

public final class RecycleBinService {
	
	public <T> void recycle(T object) {
		System.out.println(JSON.serialize(object));
	}

	public <T> T recover(String filePath, Class<T> clazz) {
		return JSON.deserialize("", clazz);
	}

	public <T> List<T> getHistorys(Long id, Class<T> clazz) {
		return null;
	}

	public static void main(String[] ages) throws IllegalArgumentException, IllegalAccessException {
		RecycleBinService recycleBinService = new RecycleBinService();
		recycleBinService.recycle(new Object());
	}
	
}