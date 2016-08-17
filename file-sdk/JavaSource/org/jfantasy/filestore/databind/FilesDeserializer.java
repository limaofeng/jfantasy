package org.jfantasy.filestore.databind;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.jfantasy.filestore.File;
import org.jfantasy.framework.autoconfigure.ApiGatewaySettings;
import org.jfantasy.framework.jackson.JSON;
import org.jfantasy.framework.spring.SpringContextUtil;
import org.jfantasy.framework.spring.validation.RESTful;
import org.jfantasy.framework.util.common.ClassUtil;
import org.jfantasy.framework.util.common.StringUtil;

import java.io.IOException;
import java.lang.reflect.Array;

public class FilesDeserializer extends JsonDeserializer<File[]> {

    @Override
    public File[] deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        String value = jp.getValueAsString();
        String files = StringUtil.nullValue(ClassUtil.isArray(value) ? Array.get(value, 0) : value);
        File[] fileDetails;
        if (files.startsWith("[") && files.endsWith("]")) {
            fileDetails = JSON.deserialize(files, new TypeReference<File[]>() {
            });
            if (fileDetails != null) {
                return fileDetails;
            }
        }
        if (StringUtil.isBlank(files)) {
            return new File[0];
        }
        String[] keys = files.split(",");
        if (keys.length == 0) {
            return new File[0];
        }
        fileDetails = new File[keys.length];
        for (int i = 0, len = keys.length; i < len; i++) {
            File fileDetail = getFile(keys[i]);
            if (fileDetail == null) {
                continue;
            }
            fileDetails[i] = fileDetail;
        }
        return fileDetails;
    }

    private File getFile(String key) {
        ApiGatewaySettings apiGatewaySettings = SpringContextUtil.getBeanByType(ApiGatewaySettings.class);
        assert apiGatewaySettings != null;
        return RESTful.restTemplate.getForObject(apiGatewaySettings.getUrl() + "/files/" + key, File.class);
    }
}
