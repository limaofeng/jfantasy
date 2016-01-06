package org.jfantasy.pay.product;

import org.jfantasy.framework.util.common.StringUtil;

import java.util.HashMap;

public class Parameters extends HashMap<String,String> {

    public String get(String key, String defaultValue) {
        return StringUtil.defaultValue(get(key),defaultValue);
    }
}
