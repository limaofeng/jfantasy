package com.fantasy.framework.util.userstamp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.security.MessageDigest;

public class Decoder {

	private static final Log LOGGER = LogFactory.getLog(Decoder.class);

	public static UserResult decode(String userStamp) {
		if (userStamp == null) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("userStamp is NULL ");
			}
			return null;
		}
		char[] stamp = userStamp.toCharArray();
		if ((stamp.length != 16) || (!v(stamp))) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("无效的userStamp : " + userStamp);
			}
			return null;
		}
		UserResult userResult = new UserResult();
		int randomType = CToN(stamp[6]);
		userResult.setUserType((randomType & 0x30) >> 4);
		randomType &= 15;
		userResult.setRandomType(randomType);
		userResult.setCssStyle(CToN(stamp[13]) >> 3);
		char[] pwChars = new char[5];
		for (int i = 0; i < 5; i++) {
			pwChars[i] = stamp[RandomType.sequence[randomType][i]];
		}
		userResult.setPasswordHash(String.valueOf(pwChars));
		int idStop = CToN(stamp[2]) & 0x7;
		int userId = 0;
		for (int i = idStop + 4; i >= 5; i--) {
			userId = userId * 62 + CToN(stamp[RandomType.sequence[randomType][i]]);
		}
		userResult.setUserId(userId);
		return userResult;
	}

	public static boolean v(char[] userStamp) {
		try {
			MessageDigest algorithm = MessageDigest.getInstance("MD5");
			algorithm.reset();
			algorithm.update((new String(userStamp, 0, 14) + "ldg").getBytes());
			byte[] messageDigest = algorithm.digest();
			for (int i = 0; i < 2; i++){
                if (userStamp[14 + i] != Encoder.NToC(Math.abs(messageDigest[i]) % 62)){
                    return false;
                }
            }
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return false;
		}
		return true;
	}

	protected static int CToN(char c) {
		return c - ((c >= '0') && (c <= '9') ? '￼' : (c >= 'A') && (c <= 'Z') ? '\'' : (c >= 'a') && (c <= 'z') ? 'a' : c);
	}

	/*public static void main(String[] args) {

		UserStamp key = Encoder.encode(1, 50124, "123456", 5);
		System.out.println(key);

		UserResult userResult = decode(key.toString());// "HzRnCQagkhfcQdst"
		System.out.println("cssStyle : " + userResult.getCssStyle());
		System.out.println("passwordHash : " + userResult.getPasswordHash());
		System.out.println("userId : " + userResult.getUserId());
		System.out.println("checkPassword : " + userResult.checkPassword("91919191"));
		System.out.println("userType : " + userResult.getUserType());
		System.out.println(userResult.getMemKey());
	}*/
}