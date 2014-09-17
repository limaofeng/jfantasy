package com.fantasy.framework.util.common;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;

import com.fantasy.framework.util.regexp.RegexpUtil;
import com.fantasy.framework.util.regexp.RegexpUtil.AbstractReplaceCallBack;

public class I18nUtil {

	public static String unicode(String text) {
		char[] utfBytes = text.toCharArray();
		String unicodeBytes = "";
		for (int byteIndex = 0; byteIndex < utfBytes.length; byteIndex++) {
			String hexB = Integer.toHexString(utfBytes[byteIndex]);
			if (hexB.length() <= 2) {
				hexB = StringUtil.append(hexB, -4, new String[] { "0" });
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

	public static void main(String[] args) throws UnsupportedEncodingException {
		String unicode = unicode("：“成功导入{0}条,新的资源!");
		System.out.println(unicode);
		System.out.print(decodeUnicode(unicode));
	}

}