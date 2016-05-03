package org.jfantasy.framework.crypto;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.*;

public class SymmetricCrypto implements SecurityInc {
    private SecretKey secretKey = null;

    private Cipher ecipher = null;

    private Cipher dcipher = null;

    private KeyPair keypair = null;

    private PublicKey publicKey = null;

    private PrivateKey privateKey = null;

    private Signature sSignature = null;

    private Signature vSignature = null;

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public SymmetricCrypto() {
        try {
            this.secretKey = KeyGenerator.getInstance("DES").generateKey();

            this.ecipher = Cipher.getInstance("DES");
            this.ecipher.init(1, this.secretKey);

            this.dcipher = Cipher.getInstance("DES");
            this.dcipher.init(2, this.secretKey);

            this.keypair = generatorKeyPair();
            this.privateKey = this.keypair.getPrivate();
            this.publicKey = this.keypair.getPublic();

            this.sSignature = Signature.getInstance("DSA");
            this.sSignature.initSign(this.privateKey);

            this.vSignature = Signature.getInstance("DSA");
            this.vSignature.initVerify(this.publicKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private KeyPair generatorKeyPair()
            throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA");
        keyGen.initialize(1024, new SecureRandom());
        return keyGen.generateKeyPair();
    }

    public byte[] encrypt(byte[] data) throws Exception {
        return this.ecipher.doFinal(data);
    }

    public byte[] decrypt(byte[] data) throws Exception {
        return this.dcipher.doFinal(data);
    }

    public byte[] signature(byte[] data)
            throws Exception {
        this.sSignature.update(data);
        return this.sSignature.sign();
    }

    public boolean verify(byte[] buffer, byte[] signData)
            throws Exception {
        this.vSignature.update(buffer);
        return this.vSignature.verify(signData);
    }

}