package com.fantasy.framework.util.common;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class MessageDigestUtil {

    private static Map<String, MessageDigestUtil> md5utils = new HashMap<String, MessageDigestUtil>();

    private static final int PAD_BELOW = 0x10;
    private static final int TWO_BYTES = 0xFF;

    private MessageDigest messagedigest = null;
    private static final Log LOGGER = LogFactory.getLog(MessageDigestUtil.class);

    private String algorithm;

    public void afterPropertiesSet() {
        try {
            if (md5utils.containsKey(this.algorithm)) {
                messagedigest = md5utils.get(this.algorithm).messagedigest;
            } else {
                messagedigest = MessageDigest.getInstance(this.algorithm);
                md5utils.put(this.algorithm, this);
            }
        } catch (NoSuchAlgorithmException nsaex) {
            LOGGER.error(MessageDigestUtil.class.getName() + "初始化失败，MessageDigest不支持MD5Utils。", nsaex);
        }
    }

    public static MessageDigestUtil getInstance() {
        return getInstance("MD5");
    }

    public static MessageDigestUtil getInstance(String algorithm) {
        if (!md5utils.containsKey(algorithm)) {
            MessageDigestUtil md5Utils = new MessageDigestUtil();
            md5Utils.setAlgorithm(algorithm);
            md5Utils.afterPropertiesSet();
        }
        return md5utils.get(algorithm);
    }

    public String get(File file) throws IOException {
        return get(new FileInputStream(file));
    }

    public String get(InputStream in) throws IOException {
        byte[] buffer = new byte[2048];
        int numRead;
        while ((numRead = in.read(buffer)) != -1) {
            messagedigest.update(buffer, 0, numRead);
        }
        StreamUtil.closeQuietly(in);
        return bufferToHex(messagedigest.digest());
    }

    public String get(String s) {
        return bufferToHex(get(s.getBytes()));
    }

    public byte[] get(byte[] bytes) {
        messagedigest.update(bytes);
        return messagedigest.digest();
    }

    public String bufferToHex(byte bytes[]) {
        return bufferToHex(bytes, 0, bytes.length);
    }

    private String bufferToHex(byte bytes[], int m, int n) {
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;
        for (int l = m; l < k; l++) {
            appendHexPair(bytes[l], stringbuffer);
        }
        return stringbuffer.toString();
    }

    private void appendHexPair(byte bt, StringBuffer stringbuffer) {
        int b = bt & TWO_BYTES;
        if (b < PAD_BELOW) {
            stringbuffer.append('0');
        }
        stringbuffer.append(Integer.toHexString(b));
    }

    public boolean check(String password, String md5PwdStr) {
        String s = get(password);
        return s.equals(md5PwdStr);
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

}
