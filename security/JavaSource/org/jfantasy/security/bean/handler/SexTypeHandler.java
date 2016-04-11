package org.jfantasy.security.bean.handler;

import org.apache.commons.beanutils.Converter;
import org.jfantasy.framework.dao.mybatis.type.CharEnumTypeHandler;
import org.jfantasy.security.bean.enums.Sex;

public class SexTypeHandler extends CharEnumTypeHandler<Sex> {
    public SexTypeHandler() {
        super(new Converter() {

            @SuppressWarnings("rawtypes")
            public Object convert(Class classes, Object value) {
                if (value instanceof Sex) {
                    switch (((Sex) value).ordinal()) {
                        case 0:
                            return 'U';
                        case 1:
                            return 'M';
                        case 2:
                            return 'F';
                        default:

                    }
                    return 'U';
                }
                if (value instanceof String) {
                    switch (((String) value).charAt(0)) {
                        case 'F':
                            return Sex.female;
                        case 'M':
                            return Sex.male;
                        default:
                            return 'U';
                    }
                }
                return 'U';
            }
        });
    }
}