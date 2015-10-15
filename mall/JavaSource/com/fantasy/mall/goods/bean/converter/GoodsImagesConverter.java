package com.fantasy.mall.goods.bean.converter;


import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.framework.util.jackson.JSON;
import com.fantasy.mall.goods.bean.GoodsImage;

import javax.persistence.AttributeConverter;

public class GoodsImagesConverter implements AttributeConverter<GoodsImage[], String> {

    @Override
    public String convertToDatabaseColumn(GoodsImage[] goodsImages) {
        if (goodsImages == null || goodsImages.length == 0) {
            return null;
        }
        return JSON.serialize(goodsImages);
    }

    @Override
    public GoodsImage[] convertToEntityAttribute(String dbData) {
        if (StringUtil.isBlank(dbData)) {
            return new GoodsImage[0];
        }
        return ObjectUtil.defaultValue(JSON.deserialize(dbData, GoodsImage[].class), new GoodsImage[0]);
    }

}
