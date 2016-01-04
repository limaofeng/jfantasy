package org.jfantasy.framework.util.common;

import org.apache.commons.codec.binary.Base64;

public class Base64Util {

    private Base64Util(){

    }
    public static byte[] encode(byte[] data) {
        return Base64.encodeBase64(data);
    }

    public static byte[] decode(byte[] data) {
        return Base64.decodeBase64(data);
    }

}
