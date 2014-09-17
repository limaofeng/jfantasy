package com.fantasy.framework.util.jm;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import javax.crypto.Cipher;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.EncryptionException;

public class RSAUtil {
	public static KeyPair generateKeyPair() throws EncryptionException {
		try {
			KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA", new BouncyCastleProvider());

			int KEY_SIZE = 1024;

			keyPairGen.initialize(1024, new SecureRandom());

			KeyPair keyPair = keyPairGen.genKeyPair();

			return keyPair;
		} catch (Exception e) {
			throw new EncryptionException(e.getMessage());
		}

	}

	public static RSAPublicKey generateRSAPublicKey(byte[] modulus, byte[] publicExponent) throws EncryptionException {
		KeyFactory keyFac = null;
		try {
			keyFac = KeyFactory.getInstance("RSA", new BouncyCastleProvider());
		} catch (NoSuchAlgorithmException ex) {
			throw new EncryptionException(ex.getMessage());
		}

		RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(new BigInteger(modulus), new BigInteger(publicExponent));
		try {
			return (RSAPublicKey) keyFac.generatePublic(pubKeySpec);
		} catch (InvalidKeySpecException ex) {
			throw new EncryptionException(ex.getMessage());
		}

	}

	public static RSAPrivateKey generateRSAPrivateKey(byte[] modulus, byte[] privateExponent) throws EncryptionException {
		KeyFactory keyFac = null;
		try {
			keyFac = KeyFactory.getInstance("RSA", new BouncyCastleProvider());
		} catch (NoSuchAlgorithmException ex) {
			throw new EncryptionException(ex.getMessage());
		}

		RSAPrivateKeySpec priKeySpec = new RSAPrivateKeySpec(new BigInteger(modulus), new BigInteger(privateExponent));
		try {
			return (RSAPrivateKey) keyFac.generatePrivate(priKeySpec);
		} catch (InvalidKeySpecException ex) {
			throw new EncryptionException(ex.getMessage());
		}

	}

	public static byte[] encrypt(Key key, byte[] data) throws EncryptionException {
		try {
			Cipher cipher = Cipher.getInstance("RSA", new BouncyCastleProvider());

			cipher.init(1, key);

			int blockSize = cipher.getBlockSize();

			int outputSize = cipher.getOutputSize(data.length);

			int leavedSize = data.length % blockSize;

			int blocksSize = leavedSize != 0 ? data.length / blockSize + 1 : data.length / blockSize;

			byte[] raw = new byte[outputSize * blocksSize];

			int i = 0;

			while (data.length - i * blockSize > 0) {
				if (data.length - i * blockSize > blockSize) {
					cipher.doFinal(data, i * blockSize, blockSize, raw, i * outputSize);
				} else {
					cipher.doFinal(data, i * blockSize, data.length - i * blockSize, raw, i * outputSize);
				}

				i++;
			}

			return raw;
		} catch (Exception e) {
			throw new EncryptionException(e.getMessage());
		}

	}

	public static byte[] decrypt(Key key, byte[] raw) throws EncryptionException {
		try {
			Cipher cipher = Cipher.getInstance("RSA", new BouncyCastleProvider());

			cipher.init(2, key);

			int blockSize = cipher.getBlockSize();

			ByteArrayOutputStream bout = new ByteArrayOutputStream(64);

			int j = 0;

			while (raw.length - j * blockSize > 0) {
				bout.write(cipher.doFinal(raw, j * blockSize, blockSize));

				j++;
			}

			return bout.toByteArray();
		} catch (Exception e) {
			throw new EncryptionException(e.getMessage());
		}

	}

	public static void main(String[] args) throws Exception {
		byte[] orgData = "sdfds".getBytes();

		KeyPair keyPair = generateKeyPair();

		RSAPublicKey pubKey = (RSAPublicKey) keyPair.getPublic();

		RSAPrivateKey priKey = (RSAPrivateKey) keyPair.getPrivate();

		byte[] pubModBytes = pubKey.getModulus().toByteArray();

		byte[] pubPubExpBytes = pubKey.getPublicExponent().toByteArray();

		byte[] priModBytes = priKey.getModulus().toByteArray();

		byte[] priPriExpBytes = priKey.getPrivateExponent().toByteArray();

		RSAPublicKey recoveryPubKey = generateRSAPublicKey(pubModBytes, pubPubExpBytes);

		RSAPrivateKey recoveryPriKey = generateRSAPrivateKey(priModBytes, priPriExpBytes);

		byte[] raw = encrypt(recoveryPriKey, orgData);

		System.out.println(raw.length);

		byte[] data = decrypt(recoveryPubKey, raw);

		System.out.println(new String(data));
	}
}