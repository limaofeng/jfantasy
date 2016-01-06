package org.jfantasy.framework.util.common;

import org.jfantasy.framework.util.regexp.RegexpUtil;
import org.jfantasy.framework.util.regexp.RegexpUtil.AbstractReplaceCallBack;

import java.util.regex.Matcher;

public class I18nUtil {
    private I18nUtil() {
    }

    public static String unicode(String text) {
        char[] utfBytes = text.toCharArray();
        String unicodeBytes = "";
        for (int byteIndex = 0; byteIndex < utfBytes.length; byteIndex++) {
            String hexB = Integer.toHexString(utfBytes[byteIndex]);
            if (hexB.length() <= 2) {
                hexB = StringUtil.append(hexB, -4, new String[]{"0"});
            }
            unicodeBytes += (StringUtil.isChinese(String.valueOf(utfBytes[byteIndex])) ? "\\u" + hexB : String.valueOf(utfBytes[byteIndex]));
        }
        return unicodeBytes;
    }

    public static String decodeUnicode(String text) {
        return RegexpUtil.replace(text, "\\\\u[0-9a-zA-Z]{4}", new AbstractReplaceCallBack() {

            @Override
            public String doReplace(String text, int index, Matcher matcher) {
                return String.valueOf((char) Integer.parseInt(text.substring(2), 16));
            }

        });
    }



}