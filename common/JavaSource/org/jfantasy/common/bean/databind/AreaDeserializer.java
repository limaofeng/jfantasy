package org.jfantasy.common.bean.databind;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.jfantasy.common.bean.Area;
import org.jfantasy.common.service.AreaService;
import org.jfantasy.framework.spring.SpringContextUtil;
import org.jfantasy.framework.util.common.StringUtil;

import java.io.IOException;

public class AreaDeserializer extends JsonDeserializer<Area> {

    private static AreaService areaService;

    @Override
    public Area deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        String id = jp.getValueAsString();
        return StringUtil.isNotBlank(id) ? getAreaService().get(id) : null;
    }

    private AreaService getAreaService() {
        return areaService == null ? areaService = SpringContextUtil.getBeanByType(AreaService.class) : areaService;
    }

}