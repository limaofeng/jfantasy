package org.jfantasy.common.bean.converter;

import org.jfantasy.common.bean.enums.AreaTag;
import org.junit.Assert;
import org.junit.Test;

public class AreaTagsConverterTest {

    private AreaTagsConverter converter = new AreaTagsConverter();

    @Test
    public void convertToDatabaseColumn() throws Exception {
        AreaTag[] tags = new AreaTag[]{AreaTag.city,AreaTag.region};
        String _tags = converter.convertToDatabaseColumn(tags);
        Assert.assertEquals(_tags,"city;region");
    }

    @Test
    public void convertToEntityAttribute() throws Exception {
        AreaTag[] tags  = converter.convertToEntityAttribute("city;region");
        Assert.assertArrayEquals(tags,new AreaTag[]{AreaTag.city,AreaTag.region});
    }

}