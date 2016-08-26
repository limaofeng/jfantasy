package org.jfantasy.pay.product.sign;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.framework.util.web.WebUtil;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class SignUtil {

    private static final Log LOG = LogFactory.getLog(SignUtil.class);

    public static String coverMapString(Map<String, String> data, String... ignoreFields) {
        TreeMap<String, String> tree = new TreeMap<>();
        for (Map.Entry<String, String> entry : data.entrySet()) {
            if (ObjectUtil.exists(ignoreFields, entry.getKey())) {
                continue;
            }
            if (StringUtil.isBlank(entry.getValue())) {
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

    public static Map<String, String> parseQuery(String query, boolean transform) {
        Map<String, String> params = new LinkedHashMap<String, String>();
        if (StringUtil.isBlank(query)) {
            return params;
        }
        for (String pair : query.split("[;&]")) {
            int index = pair.indexOf("=");
            String key = pair.substring(0, index);
            String val = pair.substring(index + 1);
            if (StringUtil.isNotBlank(val)) {
                String newVal = val;
                if (transform && Charset.forName("ASCII").newEncoder().canEncode(val)) {
                    newVal = StringUtil.decodeURI(val, "utf-8");
                    if (!newVal.equals(val)) {
                        LOG.debug(key + " 的原始编码为[ASCII]转编码:" + val + "=>" + newVal);
                    }
                } else if (transform && Charset.forName("ISO-8859-1").newEncoder().canEncode(val)) {
                    newVal = WebUtil.transformCoding(val, "ISO-8859-1", "utf-8");
                    if (!newVal.equals(val)) {
                        LOG.debug(key + " 的原始编码为[ISO-8859-1]转编码:" + val + "=>" + newVal);
                    }
                }
                if (!newVal.equals(val)) {
                    if ("signature".equals(key) || "sign".equals(key)) {
                        newVal = newVal.replaceAll("%2B","+").replaceAll(" ", "+");
                    }
                    val = newVal;
                }
            }
            params.put(key, val);
        }
        return params;
    }

    public static String encodeBase64(byte[] sign, String charset) throws UnsupportedEncodingException {
        return new String(Base64.encodeBase64(sign), charset);
    }

    public static byte[] decodeBase64(String sign, String charset) throws UnsupportedEncodingException {
        return Base64.decodeBase64(sign.getBytes(charset));
    }

}
