package org.jfantasy.framework.dao.hibernate.converter;

import org.jfantasy.framework.crypto.DESPlus;

import javax.persistence.AttributeConverter;

public class DESConverter implements AttributeConverter<String, String> {

    private static DESPlus desPlus;

    public DESConverter() {
        desPlus = new DESPlus();
    }

    public DESConverter(String key) {
        desPlus = new DESPlus(key);
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        return desPlus.encrypt(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        return desPlus.decrypt(dbData);
    }
}
