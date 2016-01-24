package org.jfantasy.framework.dao.hibernate.converter;

import org.apache.log4j.Logger;
import org.junit.Test;


public class DESConverterTest {

    private DESConverter converter = new DESConverter("hooluesoft");

    private static Logger LOGGER = Logger.getLogger(DESConverterTest.class);

    @Test
    public void testConvertToDatabaseColumn() throws Exception {
        LOGGER.debug(converter.convertToDatabaseColumn("中文"));

        LOGGER.debug(converter.convertToDatabaseColumn("李茂峰"));

        LOGGER.debug(converter.convertToDatabaseColumn("15921884771"));

        LOGGER.debug(new DESConverter().convertToDatabaseColumn(new String(new byte[]{63,63,63},"utf-8")));
        LOGGER.debug(new DESConverter().convertToDatabaseColumn("李茂峰"));
    }

    @Test
    public void testConvertToEntityAttribute() throws Exception {
        LOGGER.debug(converter.convertToEntityAttribute("d699ef0e932711e6"));

        LOGGER.debug(converter.convertToEntityAttribute("11196231db5fc99eff08925a14c3dae5"));

        LOGGER.debug(converter.convertToEntityAttribute("4ae53296a74f57280e2c7067d5a53bba"));

        LOGGER.debug(new DESConverter().convertToEntityAttribute("fefd3fca92327867"));
    }

}