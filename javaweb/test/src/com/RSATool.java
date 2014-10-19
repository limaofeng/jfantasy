package com;

import com.fantasy.framework.util.common.Base64Util;

import javax.crypto.Cipher;
import java.io.*;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class RSATool {

    public static void makekeyfile(String pubkeyfile, String privatekeyfile) throws NoSuchAlgorithmException, IOException {
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        // 初始化密钥对生成器，密钥大小为1024位
        keyPairGen.initialize(1024);
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();

        // 得到私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        // 得到公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

        // 生成私钥
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(privatekeyfile));
        oos.writeObject(privateKey);
        oos.flush();
        oos.close();

        oos = new ObjectOutputStream(new FileOutputStream(pubkeyfile));
        oos.writeObject(publicKey);
        oos.flush();
        oos.close();

        System.out.println("make file ok!");
    }

    /**
     * @param k       key
     * @param data    data[]
     * @param encrypt 1 加密 0解密
     * @return byte[]
     * @throws Exception
     */
    public static byte[] handleData(Key k, byte[] data, int encrypt) throws Exception {
        if (k != null) {
            Cipher cipher = Cipher.getInstance("RSA");
            if (encrypt == 1) {
                cipher.init(Cipher.ENCRYPT_MODE, k);
                return cipher.doFinal(data);
            } else if (encrypt == 0) {
                cipher.init(Cipher.DECRYPT_MODE, k);
                return cipher.doFinal(data);
            } else {
                System.out.println("参数必须为: 1 加密 0解密");
            }
        }
        return null;
    }

    public static void main(String[] args) throws Exception {

        String pubfile = "/users/lmf/Downloads/keys/pub.key";//"/users/lmf/Downloads/keys/client.jks";
        String prifile = "/users/lmf/Downloads/keys/pri.key";//"/users/lmf/Downloads/keys/service.jks";

//        makekeyfile(pubfile, prifile);

        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(pubfile));
        RSAPublicKey pubkey = (RSAPublicKey) ois.readObject();
        ois.close();

        ois = new ObjectInputStream(new FileInputStream(prifile));
        RSAPrivateKey prikey = (RSAPrivateKey) ois.readObject();
        ois.close();

        // 使用公钥加密
        String msg = "~O(∩_∩)O哈哈~";
        String enc = "UTF-8";

        // 使用公钥加密私钥解密
        System.out.println("原文: " + msg);
        byte[] result = handleData(pubkey, msg.getBytes(enc), 1);
        System.out.println("加密: " + new String(Base64Util.encode(result),enc));
        byte[] deresult = handleData(prikey, result, 0);
        System.out.println("解密: " + new String(deresult, enc));

        msg = "嚯嚯";
        // 使用私钥加密公钥解密
        System.out.println("原文: " + msg);
        byte[] result2 = handleData(prikey, msg.getBytes(enc), 1);
        System.out.println("加密: " + new String(Base64Util.encode(result2), enc));
        byte[] deresult2 = handleData(pubkey, result2, 0);
        System.out.println("解密: " + new String(deresult2, enc));

    }

}
