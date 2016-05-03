package org.jfantasy.framework.crypto;

import org.junit.Test;
import sun.misc.BASE64Encoder;


public class ThreeDESUtilTest {

    @Test
    public void des3EncodeOrDecode() throws Exception {
        byte[] key = "6C4E60E55552386C759569836DC0F83869836DC0F838C0F71".getBytes();
        byte[] keyiv = {1, 2, 3, 4, 5, 6, 7, 8};
        byte[] data = "amigoxie".getBytes("UTF-8");
        System.out.println("data.length=" + data.length);
        System.out.println("CBC加密解密");
        byte[] str5 = ThreeDESUtil.des3EncodeCBC(key, keyiv, data);
        System.out.println(new BASE64Encoder().encode(str5));

        byte[] str6 = ThreeDESUtil.des3DecodeCBC(key, keyiv, str5);
        System.out.println(new String(str6, "UTF-8"));
    }

}