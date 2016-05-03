package org.jfantasy.framework.crypto;

import org.junit.Test;


public class GzipEncodeTest {
    @Test
    public void encode() throws Exception {
        String ins = "测试GZIP编码";

        byte[] b = GzipEncode.gzip(ins);

        DESPlus desPlus = new DESPlus();

        DESPlus desPlus2 = new DESPlus("wangchongan");
        String e2 = desPlus2.encrypt("13588888888");
        System.out.println(e2);
        String d2 = desPlus2.decrypt(e2);
        System.out.println(d2);

        System.out.println(GzipEncode.JM(GzipEncode.KL(new String(b))));

        System.out.println(new String(GzipEncode.jUnZip(b)));
    }

}