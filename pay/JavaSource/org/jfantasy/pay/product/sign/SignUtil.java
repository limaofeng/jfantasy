package org.jfantasy.pay.product.sign;


import org.apache.commons.codec.binary.Base64;
import org.jfantasy.framework.util.common.ObjectUtil;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.TreeMap;

public class SignUtil {

    public static String coverMapString(Map<String, String> data, String... ignoreFields) {
        TreeMap<String, String> tree = new TreeMap<String, String>();
        for (Map.Entry<String, String> entry : data.entrySet()) {
            if (ObjectUtil.exists(ignoreFields, entry.getKey())) {
                continue;
            }
            tree.put(entry.getKey(), entry.getValue());
        }
        StringBuilder sf = new StringBuilder();
        for (Map.Entry<String, String> entry : tree.entrySet()) {
            sf.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        return sf.substring(0, sf.length() - 1);
    }

    public static String encodeBase64(byte[] sign, String charset) throws UnsupportedEncodingException {
        return new String(Base64.encodeBase64(sign),charset);
    }
}
