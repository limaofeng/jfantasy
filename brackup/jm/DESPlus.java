package com.fantasy.framework.util.jm;

import com.sun.crypto.provider.SunJCE;
import java.io.PrintStream;
import java.security.Key;
import java.security.Security;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class DESPlus {
	private static String strDefaultKey = "szlcsoft";
	private Cipher encryptCipher = null;
	private Cipher decryptCipher = null;

	public static String byteArr2HexStr(byte[] arrB) throws Exception {
		int iLen = arrB.length;

		StringBuffer sb = new StringBuffer(iLen * 2);
		for (int i = 0; i < iLen; i++) {
			int intTmp = arrB[i];

			while (intTmp < 0) {
				intTmp += 256;
			}

			if (intTmp < 16) {
				sb.append("0");
			}
			sb.append(Integer.toString(intTmp, 16));
		}
		return sb.toString();
	}

	public static byte[] hexStr2ByteArr(String strIn) throws Exception {
		byte[] arrB = strIn.getBytes();
		int iLen = arrB.length;

		byte[] arrOut = new byte[iLen / 2];
		for (int i = 0; i < iLen; i += 2) {
			String strTmp = new String(arrB, i, 2);
			arrOut[(i / 2)] = (byte) Integer.parseInt(strTmp, 16);
		}
		return arrOut;
	}

	public DESPlus() throws Exception {
		this(strDefaultKey);
	}

	public DESPlus(String strKey) throws Exception {
		Security.addProvider(new SunJCE());
		Key key = getKey(strKey.getBytes());

		this.encryptCipher = Cipher.getInstance("DES");
		this.encryptCipher.init(1, key);

		this.decryptCipher = Cipher.getInstance("DES");
		this.decryptCipher.init(2, key);
	}

	public byte[] encrypt(byte[] arrB) throws Exception {
		return this.encryptCipher.doFinal(arrB);
	}

	public String encrypt(String strIn) throws Exception {
		return byteArr2HexStr(encrypt(strIn.getBytes()));
	}

	public byte[] decrypt(byte[] arrB) throws Exception {
		return this.decryptCipher.doFinal(arrB);
	}

	public String decrypt(String strIn) throws Exception {
		return new String(decrypt(hexStr2ByteArr(strIn)));
	}

	private Key getKey(byte[] arrBTmp) throws Exception {
		byte[] arrB = new byte[8];
		for (int i = 0; (i < arrBTmp.length) && (i < arrB.length); i++) {
			arrB[i] = arrBTmp[i];
		}

		return new SecretKeySpec(arrB, "DES");
	}

	public static void main(String[] args) throws Exception {
		DESPlus desPlus = new DESPlus();
		String e = desPlus.encrypt("开始测试123");
		System.out.println("加密:" + e);
		String d = desPlus.decrypt(e);
		System.out.println("解密:" + d);

		DESPlus desPlus2 = new DESPlus("wangchongan");
		String e2 = desPlus2.encrypt("13588888888");
		System.out.println(e2);
		String d2 = desPlus2.decrypt(e2);
		System.out.println(d2);

		System.out.println("===================");

		String ts = "开始测试123sdfsd是东方时代发送地方士大夫";
		byte[] bs = GzipEncode.gzip(ts);

		System.out.println("字节码:" + Arrays.toString(bs));
		byte[] enbs = desPlus.encrypt(bs);
		String enstr = byteArr2HexStr(enbs);
		System.out.println("加密后的串:" + enstr.toUpperCase());
		byte[] debs = desPlus.decrypt(hexStr2ByteArr(enstr));

		System.out.println(new String(GzipEncode.jUnZip(bs)));
	}
}