package com.fantasy.mall.goods.bean.converter;


import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.framework.util.jackson.JSON;
import com.fantasy.mall.goods.bean.GoodsImage;

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
