package org.jfantasy.common.databind;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.jfantasy.common.Area;
import org.jfantasy.framework.autoconfigure.ApiGatewaySettings;
import org.jfantasy.framework.spring.SpringContextUtil;
import org.jfantasy.framework.spring.validation.RESTful;
import org.jfantasy.framework.util.common.StringUtil;

import java.io.IOException;

public class AreaDeserializer extends JsonDeserializer<Area> {

    @Override
    public Area deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        String id = jp.getValueAsString();
        return StringUtil.isNotBlank(id) ? getArea(id) : null;
    }

    private Area getArea(String id) {
        ApiGatewaySettings apiGatewaySettings = SpringContextUtil.getBeanByType(ApiGatewaySettings.class);
        assert apiGatewaySettings != null;
        return RESTful.restTemplate.getForObject(apiGatewaySettings.getUrl() + "/areas/" + id, Area.class);
    }

}
