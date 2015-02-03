package com.fantasy.wx.account;

import com.fantasy.framework.util.common.PropertiesHelper;

public class Consts {
    public final static String appid= PropertiesHelper.load("props/application.properties").getProperty("weixin.appid");
}
