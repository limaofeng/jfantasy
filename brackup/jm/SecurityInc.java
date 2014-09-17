package com.fantasy.framework.util.jm;

public abstract interface SecurityInc {
	public static final String CRYPTO_FORM = "RSA/NONE/PKCS1PADDING";
	public static final String SIGNATURE_FORM = "MD5WithRSA";
	public static final String ARITHMETIC_RSA = "RSA";
	public static final String ARITHMETIC_DES = "DES";
	public static final String ARITHMETIC_DSA = "DSA";
	public static final int KEY_SIZE = 1024;

	public abstract byte[] encrypt(byte[] paramArrayOfByte) throws Exception;

	public abstract byte[] decrypt(byte[] paramArrayOfByte) throws Exception;

	public abstract byte[] signature(byte[] paramArrayOfByte) throws Exception;

	public abstract boolean verify(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2) throws Exception;
}