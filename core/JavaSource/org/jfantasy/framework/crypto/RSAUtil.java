package org.jfantasy.framework.crypto;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.EncryptionException;
import org.jfantasy.framework.util.common.StringUtil;
import org.springframework.util.Base64Utils;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

public class RSAUtil {

    private static final int KEY_SIZE = 1024;

    public static KeyPair generateKeyPair() throws EncryptionException {
        try {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA", new BouncyCastleProvider());
            keyPairGen.initialize(KEY_SIZE, new SecureRandom());
            return keyPairGen.genKeyPair();
        } catch (Exception e) {
            throw new EncryptionException(e.getMessage());
        }
    }

    public static RSAPublicKey getRSAPublicKey(String key) throws EncryptionException {
        byte[] modulus = new BigInteger(StringUtil.trim(key.split("\n")[1].split(":")[1]), 16).toByteArray();
        byte[] publicExponent = new BigInteger(StringUtil.trim(key.split("\n")[2].split(":")[1]), 16).toByteArray();
        return generateRSAPublicKey(modulus, publicExponent);
    }

    public static RSAPublicKey generateRSAPublicKey(byte[] modulus, byte[] publicExponent) throws EncryptionException {
        KeyFactory keyFac;
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

    public static RSAPrivateKey getRSAPrivateKey(String key) throws EncryptionException {
        byte[] modulus = new BigInteger(StringUtil.trim(key.split("\n")[1].split(":")[1]), 16).toByteArray();
        byte[] privateExponent = new BigInteger(StringUtil.trim(key.split("\n")[3].split(":")[1]), 16).toByteArray();
        return generateRSAPrivateKey(modulus, privateExponent);
    }

    protected static RSAPrivateKey generateRSAPrivateKey(byte[] modulus, byte[] privateExponent) throws EncryptionException {
        KeyFactory keyFac;
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

    public static String encrypt(Key key, String data) throws EncryptionException {
        return Base64Utils.encodeToString(encrypt(key, data.getBytes()));
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

    public static String decrypt(Key key, String raw) throws EncryptionException {
        return new String(decrypt(key, Base64Utils.decodeFromString(raw)));
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

}