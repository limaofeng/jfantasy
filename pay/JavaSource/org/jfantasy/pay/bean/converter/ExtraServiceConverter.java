package org.jfantasy.pay.bean.converter;

import org.jfantasy.framework.jackson.JSON;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.pay.bean.ExtraService;

import javax.persistence.AttributeConverter;

/**
 * 附件服务
 */
public class ExtraServiceConverter implements AttributeConverter<ExtraService[], String> {

    @Override
    public String convertToDatabaseColumn(ExtraService[] attribute) {
        if (attribute == null) {
            return null;
        }
        return JSON.serialize(attribute);
    }

    @Override
    public ExtraService[] convertToEntityAttribute(String dbData) {
        if (StringUtil.isBlank(dbData)) {
            return null;
        }
        return JSON.deserialize(dbData, ExtraService[].class);
    }

}
