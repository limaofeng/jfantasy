package org.jfantasy.mall.goods.bean.converter;


import org.jfantasy.framework.jackson.JSON;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.mall.goods.bean.GoodsImage;

import javax.persistence.AttributeConverter;

public class GoodsImageConverter implements AttributeConverter<GoodsImage, String> {

    @Override
    public String convertToDatabaseColumn(GoodsImage goodsImage) {
        if (goodsImage == null) {
            return null;
        }
        return JSON.serialize(goodsImage);
    }

    @Override
    public GoodsImage convertToEntityAttribute(String dbData) {
        if (StringUtil.isBlank(dbData)) {
            return null;
        }
        return JSON.deserialize(dbData, GoodsImage.class);
    }

}
