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

	public static void main(String[] args) {
		String pwd1 = "123";
		String pwd2 = "";
		CipherUtil cipher = new CipherUtil();
		System.out.println("未加密的密码:" + pwd1);

		pwd2 = generatePassword(pwd1);
		System.out.println("加密后的密码:" + pwd2);

		System.out.print("验证密码是否下确:");
		if (validatePassword(pwd2, pwd1)) {
			System.out.println("正确");
		} else
			System.out.println("错误");
	}
}
