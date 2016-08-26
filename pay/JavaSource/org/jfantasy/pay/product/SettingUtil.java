package org.jfantasy.pay.product;

import org.jfantasy.framework.util.common.PropertiesHelper;

public class SettingUtil {

    @Deprecated
    static String getServerUrl() {
        PropertiesHelper helper = PropertiesHelper.load("application.properties");
        return helper.getProperty("paynotify.url", "http://localhost");
    }

}
