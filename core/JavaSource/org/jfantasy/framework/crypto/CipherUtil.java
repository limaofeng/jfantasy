package org.jfantasy.framework.crypto;

import java.security.MessageDigest;

public class CipherUtil {
	private static final String[] hexDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

	public static String generatePassword(String inputString) {
		return encodeByMD5(inputString);
	}

	public static boolean validatePassword(String password, String inputString) {
		return password.equals(encodeByMD5(inputString));
	}

	public static String returnEncodeByMde(String originString) {
		return encodeByMD5(originString);
	}

	private static String encodeByMD5(String originString) {
		if (originString != null) {
			try {
				MessageDigest md = MessageDigest.getInstance("MD5");

				byte[] results = md.digest(originString.getBytes());

				String resultString = byteArrayToHexString(results);
				String pass = resultString.toUpperCase();
				return pass;
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return null;
	}

	private static String byteArrayToHexString(byte[] b) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString();
	}

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n += 256;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

}
