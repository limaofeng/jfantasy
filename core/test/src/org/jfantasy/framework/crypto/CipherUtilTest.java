package org.jfantasy.framework.crypto;

import org.junit.Test;

public class CipherUtilTest {

    @Test
    public void generatePassword() throws Exception {
        String pwd1 = "123";
        String pwd2 = "";
        CipherUtil cipher = new CipherUtil();
        System.out.println("未加密的密码:" + pwd1);

        pwd2 = CipherUtil.generatePassword(pwd1);
        System.out.println("加密后的密码:" + pwd2);

        System.out.print("验证密码是否下确:");
        if (CipherUtil.validatePassword(pwd2, pwd1)) {
            System.out.println("正确");
        } else {
            System.out.println("错误");
        }
    }

    @Test
    public void validatePassword() throws Exception {

    }

}