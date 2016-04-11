package org.jfantasy.framework.crypto;

import java.security.MessageDigest;

public class Test_MD5 {
	public static final String MD5(String s) {
		char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] strTemp = s.getBytes();

			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char[] str = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte b = md[i];

				str[(k++)] = hexDigits[(b >> 4 & 0xF)];
				str[(k++)] = hexDigits[(b & 0xF)];
			}
			return new String(str);
		} catch (Exception e) {
		}
		return null;
	}

	public static void main(String[] args) {
		System.out.println(MD5("123"));
		System.out.println("caidao的MD5加密后：\n" + MD5("caidao"));
		System.out.println("http://www.baidu.com/的MD5加密后：\n" + MD5("http://www.baidu.com/"));
	}
}