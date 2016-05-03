package org.jfantasy.framework.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfantasy.framework.jackson.JSON;

import java.util.List;

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


}