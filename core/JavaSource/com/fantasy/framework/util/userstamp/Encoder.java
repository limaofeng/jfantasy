package com.fantasy.framework.util.userstamp;

import com.fantasy.framework.error.IgnoreException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class Encoder {
	private static char[] genV(char[] userStamp) {
		try {
			MessageDigest algorithm = MessageDigest.getInstance("MD5");
			algorithm.reset();
			algorithm.update((new String(userStamp, 0, 14) + "ldg").getBytes());
			byte[] messageDigest = algorithm.digest();
			for (int i = 0; i < 2; i++){
                userStamp[(14 + i)] = NToC(Math.abs(messageDigest[i]) % 62);
            }
		} catch (NoSuchAlgorithmException e) {
			throw new IgnoreException(e.getMessage(),e);
		}
		return userStamp;
	}

	public static String changeCssStyle(String userStamp, int cssStyle) {
		char[] stamp = userStamp.toCharArray();
		Random r = new Random();
		stamp[12] = NToC(r.nextInt(62));
		stamp[13] = NToC(cssStyle << 3 | r.nextInt(5));
		return String.valueOf(genV(stamp));
	}

	public static String changeRandomValue(String userStamp) {
		char[] stamp = userStamp.toCharArray();
		Random r = new Random();
		stamp[12] = NToC(r.nextInt(62));
		stamp[13] = NToC(Decoder.CToN(stamp[13]) & 0x38 | r.nextInt(5));
		return String.valueOf(genV(stamp));
	}

	public static UserStamp encode(int userType, int userId, String password, int cssStyle) {
		Random r = new Random();
		int randomType = r.nextInt(15);
		return encode(userType, userId, password, randomType, cssStyle);
	}

	public static UserStamp encode(int userType, int userId, String password, int randomType, int cssStyle) {
		char[] stamp = new char[16];
		Random r = new Random();
		stamp[6] = NToC(randomType | userType << 4);
		char[] pwChars = hashPassword(password);
		for (int i = 0; i < 5; i++) {
			stamp[RandomType.sequence[randomType][i]] = pwChars[i];
		}
		int i = 5;
		do{
			stamp[RandomType.sequence[randomType][(i++)]] = NToC(userId % 62);
		}while ((userId /= 62) != 0);
		stamp[2] = NToC(i - 5 | r.nextInt(8) << 3);
		for (; i < 10; i++) {
			stamp[RandomType.sequence[randomType][i]] = NToC(r.nextInt(62));
		}
		stamp[12] = NToC(r.nextInt(62));
		stamp[13] = NToC(cssStyle << 3 | r.nextInt(5));
		UserStamp userStamp = new UserStamp();
		userStamp.setRandomType(randomType);
		userStamp.setStr(String.valueOf(genV(stamp)));
		userStamp.setPasswordHash(String.valueOf(pwChars));
		return userStamp;
	}

	public static char[] hashPassword(String password) {
		char[] pwChars = new char[5];
		try {
			MessageDigest algorithm = MessageDigest.getInstance("MD5");
			algorithm.reset();
			algorithm.update(password.getBytes());
			byte[] messageDigest = algorithm.digest();
			for (int i = 0; i < 5; i++){
                pwChars[i] = NToC(Math.abs(messageDigest[i]) % 62);
            }
		} catch (NoSuchAlgorithmException e) {
			throw new IgnoreException(e.getMessage(),e);
		}
		return pwChars;
	}

	protected static char NToC(int n) {
		return (char) (n + ((n >= 52) && (n < 62) ? -4 : (n >= 26) && (n < 52) ? 39 : (n >= 0) && (n < 26) ? 97 : -n));
	}

}