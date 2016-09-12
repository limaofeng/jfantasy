package org.jfantasy.filestore.databind;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.jfantasy.filestore.File;
import org.jfantasy.framework.autoconfigure.ApiGatewaySettings;
import org.jfantasy.framework.spring.SpringContextUtil;
import org.jfantasy.framework.spring.validation.RESTful;
import org.jfantasy.framework.util.common.StringUtil;

import java.io.IOException;

public class FileDeserializer extends JsonDeserializer<File> {

    @Override
    public File deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        String value = jp.getValueAsString();
        if (StringUtil.isBlank(value)) {
            return null;
        }
        return getFile(value);
    }

    private File getFile(String key) {
        ApiGatewaySettings apiGatewaySettings = SpringContextUtil.getBeanByType(ApiGatewaySettings.class);
        assert apiGatewaySettings != null;
        return RESTful.restTemplate.getForObject(apiGatewaySettings.getUrl() + "/files/" + key.replaceAll("[/]","-"), File.class);
    }

}
