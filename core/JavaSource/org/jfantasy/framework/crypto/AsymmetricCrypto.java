package org.jfantasy.framework.crypto;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.util.Arrays;

public class AsymmetricCrypto implements SecurityInc {
	private KeyPair keypair = null;

	private PublicKey publicKey = null;

	private PrivateKey privateKey = null;

	private Cipher ecipher = null;

	private Cipher dcipher = null;

	private Signature sSignature = null;

	private Signature vSignature = null;

	static {
		Security.addProvider(new BouncyCastleProvider());
	}

	public AsymmetricCrypto() {
		try {
			this.keypair = generatorKeyPair();
			this.privateKey = this.keypair.getPrivate();
			this.publicKey = this.keypair.getPublic();

			System.out.println("=====================");
			System.out.println(this.privateKey.getAlgorithm());
			System.out.println(this.privateKey.getFormat());
			System.out.println(Arrays.toString(this.privateKey.getEncoded()));
			System.out.println("=====================");

			System.out.println(this.publicKey.getAlgorithm());
			System.out.println(this.publicKey.getFormat());
			System.out.println(Arrays.toString(this.publicKey.getEncoded()));

			System.out.println("=====================");

			this.ecipher = Cipher.getInstance("RSA/NONE/PKCS1PADDING");
			this.ecipher.init(1, this.publicKey);

			this.dcipher = Cipher.getInstance("RSA/NONE/PKCS1PADDING");
			this.dcipher.init(2, this.privateKey);

			this.sSignature = Signature.getInstance("MD5WithRSA");
			this.sSignature.initSign(this.privateKey);

			this.vSignature = Signature.getInstance("MD5WithRSA");
			this.vSignature.initVerify(this.publicKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private KeyPair generatorKeyPair() throws Exception {
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
		keyGen.initialize(1024);
		return keyGen.genKeyPair();
	}

	public byte[] encrypt(byte[] data) throws Exception {
		int blockSize = this.ecipher.getBlockSize();
		int outputSize = this.ecipher.getOutputSize(data.length);
		int leavedSize = data.length % blockSize;
		int blocksSize = leavedSize != 0 ? data.length / blockSize + 1 : data.length / blockSize;
		byte[] encrypt = new byte[outputSize * blocksSize];
		int i = 0;
		while (data.length - i * blockSize > 0) {
			if (data.length - i * blockSize > blockSize)
				this.ecipher.doFinal(data, i * blockSize, blockSize, encrypt, i * outputSize);
			else
				this.ecipher.doFinal(data, i * blockSize, data.length - i * blockSize, encrypt, i * outputSize);
			i++;
		}

		return encrypt;
	}

	public byte[] decrypt(byte[] encryptData) throws Exception {
		byte[] decodeEncryptData = encryptData;

		int blockSize = this.dcipher.getBlockSize();
		ByteArrayOutputStream decrypt = new ByteArrayOutputStream(64);
		int j = 0;

		while (decodeEncryptData.length - j * blockSize > 0) {
			decrypt.write(this.dcipher.doFinal(decodeEncryptData, j * blockSize, blockSize));
			j++;
		}
		return decrypt.toByteArray();
	}

	public byte[] signature(byte[] data) throws Exception {
		this.sSignature.update(data);
		return this.sSignature.sign();
	}

	public boolean verify(byte[] buffer, byte[] signData) throws Exception {
		this.vSignature.update(buffer);
		return this.vSignature.verify(signData);
	}

	public static void main(String[] args) throws Exception {
		AsymmetricCrypto cryptor = new AsymmetricCrypto();

		byte[] bytes = "sdfsdfsdf".getBytes();

		byte[] encBytes = cryptor.encrypt(bytes);

		byte[] sin = cryptor.signature(encBytes);

		System.out.println(cryptor.verify(encBytes, sin));

		byte[] denc = cryptor.decrypt(encBytes);

		System.out.println(Arrays.toString(encBytes));
		System.out.println(Arrays.toString(sin));
		System.out.println(Arrays.toString(denc));

		System.out.println(new String(denc));
	}
}