package org.jfantasy.framework.crypto;

import org.junit.Test;


public class DESUtilTest {
    @Test
    public void encrypt() throws Exception {
        String source = "市规土局所属事业单位招聘20名工作人员";//"amigoxie";
        System.out.println("原文: " + source);
        String key = "A1B2C3D4E5F60708";
        String encryptData = DESUtil.encrypt(source, key);
        System.out.println("加密后: " + encryptData);
        String decryptData = DESUtil.decrypt(encryptData, key);
        System.out.println("解密后: " + decryptData);
    }

    @Test
    public void decrypt() throws Exception {

    }

}