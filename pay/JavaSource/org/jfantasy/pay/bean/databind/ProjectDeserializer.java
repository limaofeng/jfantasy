package org.jfantasy.pay.bean.databind;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.pay.bean.Project;

import java.io.IOException;

public class ProjectDeserializer extends JsonDeserializer<Project> {

    @Override
    public Project deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        String value = jp.getValueAsString();
        if (StringUtil.isBlank(value)) {
            return null;
        }
        return new Project(value);
    }

}
