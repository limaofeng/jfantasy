package com.fantasy.common.bean.databind;

import com.fantasy.common.bean.Area;
import com.fantasy.common.service.AreaService;
import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.framework.util.common.StringUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class AreaDeserialize extends JsonDeserializer<Area> {

    public static AreaService areaService;

    @Override
    public Area deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        String id = jp.getValueAsString();
        return StringUtil.isNotBlank(id) ? getAreaService().get(id) : null;
    }

    public AreaService getAreaService() {
        return areaService == null ? SpringContextUtil.getBeanByType(AreaService.class) : areaService;
    }

}