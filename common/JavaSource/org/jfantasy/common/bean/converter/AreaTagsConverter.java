package org.jfantasy.common.bean.converter;

import org.jfantasy.common.bean.enums.AreaTag;
import org.jfantasy.framework.util.common.StringUtil;

import javax.persistence.AttributeConverter;
import java.util.ArrayList;
import java.util.List;

public class AreaTagsConverter implements AttributeConverter<AreaTag[], String> {

    @Override
    public String convertToDatabaseColumn(AreaTag[] attribute) {
        if (attribute == null) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        for(AreaTag tag : attribute){
            builder.append(tag.name()).append(";");
        }
        return builder.toString().replaceAll(";$","");
    }

    @Override
    public AreaTag[] convertToEntityAttribute(String dbData) {
        if (StringUtil.isBlank(dbData)) {
            return null;
        }
        List<AreaTag> tags = new ArrayList<>();
        for(String str : StringUtil.tokenizeToStringArray(dbData,";")){
            tags.add(AreaTag.valueOf(str));
        }
        return tags.toArray(new AreaTag[tags.size()]);
    }
}