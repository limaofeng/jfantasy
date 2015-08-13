package com.fantasy.framework.service;

import java.util.List;

import com.fantasy.framework.util.jackson.JSON;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class RecycleBinService {
    private final static Log LOGGER = LogFactory.getLog(RecycleBinService.class);
    public <T> void recycle(T object) {
        LOGGER.debug(JSON.serialize(object));
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