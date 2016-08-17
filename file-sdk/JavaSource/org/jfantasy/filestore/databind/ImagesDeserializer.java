package org.jfantasy.filestore.databind;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.jfantasy.filestore.Image;
import org.jfantasy.framework.autoconfigure.ApiGatewaySettings;
import org.jfantasy.framework.spring.SpringContextUtil;
import org.jfantasy.framework.spring.validation.RESTful;
import org.jfantasy.framework.util.common.StringUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImagesDeserializer extends JsonDeserializer<Image[]> {

    @Override
    public Image[] deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        String values = jp.getValueAsString();
        if (StringUtil.isBlank(values)) {
            return null;
        }
        List<Image> images = new ArrayList<>();
        for (String value : StringUtil.tokenizeToStringArray(values)) {
            Image image = getFile(value);
            if (image == null) {
                continue;
            }
            images.add(image);
        }
        return images.toArray(new Image[images.size()]);
    }

    private Image getFile(String key) {
        ApiGatewaySettings apiGatewaySettings = SpringContextUtil.getBeanByType(ApiGatewaySettings.class);
        assert apiGatewaySettings != null;
        return RESTful.restTemplate.getForObject(apiGatewaySettings.getUrl() + "/files/" + key, Image.class);
    }

}
