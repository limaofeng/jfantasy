package org.jfantasy.framework.crypto;

import static org.junit.Assert.*;

/**
 * Created by limaofeng on 16/3/29.
 */
public class MD5andKLTest {

    @org.junit.Test
    public void MD5() throws Exception {
        String s = new String("123456");

        System.out.println(s);
        System.out.println("MD5后再加密：" + MD5andKL.KL(s));
        System.out.println("解密为MD5后的：" + MD5andKL.JM(MD5andKL.KL(s)));
    }
}