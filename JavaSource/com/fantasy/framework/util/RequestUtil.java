package com.fantasy.framework.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.ServletRequestUtils;

@Deprecated
public class RequestUtil extends ServletRequestUtils {

	public static String getStringsParameter(HttpServletRequest request, String name, String defaultVal) {
		String strResult = defaultVal;
		try {
			StringBuffer bufResult = new StringBuffer();
			String[] arrayTemp = getStringParameters(request, name);

			if ((arrayTemp != null) && (arrayTemp.length > 0)) {
				for (int num = 0; num < arrayTemp.length; num++) {
					if (num == arrayTemp.length - 1)
						bufResult.append(arrayTemp[num]);
					else {
						bufResult.append(arrayTemp[num] + ",");
					}
				}
			}
			strResult = bufResult.toString();
		} catch (Exception e) {
			strResult = defaultVal;
		}
		return strResult;
	}

	public static String getStrParam(ServletRequest request, String name, String defaultVal) throws UnsupportedEncodingException {
		String str = getStringParameter(request, name, defaultVal);
		str = new String(str.getBytes("8859_1"), "UTF-8");
		return str;
	}

	public static void main(String[] args) throws UnsupportedEncodingException {
		System.out.println(URLDecoder.decode("中文", "UTF-8"));
		System.out.println(new String("中文".getBytes("8859_1"), "UTF-8"));
	}
}
