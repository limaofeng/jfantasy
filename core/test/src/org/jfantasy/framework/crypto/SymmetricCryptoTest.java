package org.jfantasy.framework.crypto;

import org.junit.Test;


public class SymmetricCryptoTest {
    @Test
    public void encrypt() throws Exception {
        SymmetricCrypto cryptor = new SymmetricCrypto();

        byte[] bytes = "abc".getBytes();

        byte[] encBytes = cryptor.encrypt(bytes);

        byte[] sin = cryptor.signature(encBytes);

        System.out.println(cryptor.verify(encBytes, sin));

        byte[] denc = cryptor.decrypt(encBytes);

        System.out.println(new String(denc));
    }

    @Test
    public void decrypt() throws Exception {

    }

}