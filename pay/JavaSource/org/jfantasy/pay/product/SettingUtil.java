package org.jfantasy.pay.product;

import org.jfantasy.framework.util.common.PropertiesHelper;

public class SettingUtil {

    @Deprecated
    public static String getServerUrl() {
        PropertiesHelper helper = PropertiesHelper.load("application.properties");
        return helper.getProperty("server.domain", "http://localhost");
    }

}
