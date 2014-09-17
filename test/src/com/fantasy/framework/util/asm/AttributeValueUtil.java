package com.fantasy.framework.util.asm;

import com.fantasy.attr.bean.AttributeValue;
import com.fantasy.framework.util.common.ObjectUtil;

import java.util.List;


public class AttributeValueUtil {

    public static void saveValue(List<AttributeValue> attributeValues, String attributeCode, String value) {
        AttributeValue attributeValue = ObjectUtil.find(attributeValues,"attribute.code",attributeCode);
        String saveValue = value;//TODO 保存转换之后的值
        attributeValue.setValue(saveValue);
    }

}
