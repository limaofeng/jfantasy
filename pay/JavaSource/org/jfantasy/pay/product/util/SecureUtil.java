package org.jfantasy.pay.product.util;


import com.unionpay.acp.sdk.CliperInstance;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.RSAPublicKey;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class SecureUtil {
    private static final String ALGORITHM_MD5 = "MD5";
    private static final String ALGORITHM_SHA1 = "SHA-1";
    private static final String BC_PROV_ALGORITHM_SHA1RSA = "SHA1withRSA";

    public static byte[] md5(byte[] datas) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
            md.reset();
            md.update(datas);
            return md.digest();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.writeErrorLog("MD5计算失败", e);
        }
        return null;
    }

    public static byte[] sha1(byte[] data) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
            md.reset();
            md.update(data);
            return md.digest();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.writeErrorLog("SHA1计算失败", e);
        }
        return null;
    }

    public static byte[] md5X16(String datas, String encoding) {
        byte[] bytes = md5(datas, encoding);
        StringBuilder md5StrBuff = new StringBuilder();
        for (int i = 0; i < bytes.length; ++i)
            if (Integer.toHexString(0xFF & bytes[i]).length() == 1) {
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & bytes[i]));
            } else
                md5StrBuff.append(Integer.toHexString(0xFF & bytes[i]));

        try {
            return md5StrBuff.toString().getBytes(encoding);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] sha1X16(String data, String encoding) {
        byte[] bytes = sha1(data, encoding);
        StringBuilder sha1StrBuff = new StringBuilder();
        assert bytes != null;
        for (byte aByte : bytes) {
            if (Integer.toHexString(0xFF & aByte).length() == 1) {
                sha1StrBuff.append("0").append(Integer.toHexString(0xFF & aByte));
            } else
                sha1StrBuff.append(Integer.toHexString(0xFF & aByte));
        }
        try {
            return sha1StrBuff.toString().getBytes(encoding);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] md5(String datas, String encoding) {
        try {
            return md5(datas.getBytes(encoding));
        } catch (UnsupportedEncodingException e) {
            LogUtil.writeErrorLog("MD5计算失败", e);
        }
        return null;
    }

    public static byte[] sha1(String datas, String encoding) {
        try {
            return sha1(datas.getBytes(encoding));
        } catch (UnsupportedEncodingException e) {
            LogUtil.writeErrorLog("SHA1计算失败", e);
        }
        return null;
    }

    public static byte[] signBySoft(PrivateKey privateKey, byte[] data)
            throws Exception {
        byte[] result = null;
        Signature st = Signature.getInstance("SHA1withRSA");
        st.initSign(privateKey);
        st.update(data);
        result = st.sign();
        return result;
    }

    public static boolean validateSignBySoft(PublicKey publicKey, byte[] signData, byte[] srcData)
            throws Exception {
        Signature st = Signature.getInstance("SHA1withRSA");
        st.initVerify(publicKey);
        st.update(srcData);
        return st.verify(signData);
    }

    public static byte[] inflater(byte[] inputByte)
            throws IOException {
        int compressedDataLength = 0;
        Inflater compresser = new Inflater(false);
        compresser.setInput(inputByte, 0, inputByte.length);
        ByteArrayOutputStream o = new ByteArrayOutputStream(inputByte.length);
        byte[] result = new byte[1024];
        try {
            while (!(compresser.finished())) {
                compressedDataLength = compresser.inflate(result);
                if (compressedDataLength == 0)
                    break;

                o.write(result, 0, compressedDataLength);
            }
        } catch (Exception ex) {
            System.err.println("Data format error!\n");
            ex.printStackTrace();
        } finally {
            o.close();
        }
        compresser.end();
        return o.toByteArray();
    }

    public static byte[] deflater(byte[] inputByte)
            throws IOException {
        int compressedDataLength = 0;
        Deflater compresser = new Deflater();
        compresser.setInput(inputByte);
        compresser.finish();
        ByteArrayOutputStream o = new ByteArrayOutputStream(inputByte.length);
        byte[] result = new byte[1024];
        try {
            while (!(compresser.finished())) {
                compressedDataLength = compresser.deflate(result);
                o.write(result, 0, compressedDataLength);
            }
        } finally {
            o.close();
        }
        compresser.end();
        return o.toByteArray();
    }

    public static String EncryptPin(String pin, String card, String encoding, PublicKey key) {
        byte[] pinBlock = pin2PinBlockWithCardNO(pin, card);

        byte[] data = null;
        try {
            data = encryptedPin(key, pinBlock);
            return new String(base64Encode(data), encoding);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String EncryptData(String dataString, String encoding, PublicKey key) {
        byte[] data = null;
        try {
            data = encryptedPin(key, dataString.getBytes(encoding));
            return new String(base64Encode(data), encoding);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String DecryptedData(String dataString, String encoding, PrivateKey key) {
        byte[] data = null;
        try {
            data = decryptedPin(key, dataString.getBytes(encoding));
            return new String(data, encoding);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static byte[] base64Decode(byte[] inputByte)
            throws IOException {
        return Base64.decodeBase64(inputByte);
    }

    public static byte[] base64Encode(byte[] inputByte)
            throws IOException {
        return Base64.encodeBase64(inputByte);
    }

    public byte[] Str2Hex(String str) {
        char[] ch = str.toCharArray();
        byte[] b = new byte[ch.length / 2];
        for (int i = 0; i < ch.length; ++i) {
            if (ch[i] == 0)
                break;

            if ((ch[i] >= '0') && (ch[i] <= '9'))
                ch[i] = (char) (ch[i] - '0');
            else if ((ch[i] >= 'A') && (ch[i] <= 'F'))
                ch[i] = (char) (ch[i] - 'A' + 10);
        }

        for (int i = 0; i < b.length; ++i)
            b[i] = (byte) ((ch[(2 * i)] << '\4' & 0xF0) + (ch[(2 * i + 1)] & 0xF));

        return b;
    }

    public static String Hex2Str(byte[] b) {
        StringBuffer d = new StringBuffer(b.length * 2);
        for (int i = 0; i < b.length; ++i) {
            char hi = Character.forDigit(b[i] >> 4 & 0xF, 16);
            char lo = Character.forDigit(b[i] & 0xF, 16);
            d.append(Character.toUpperCase(hi));
            d.append(Character.toUpperCase(lo));
        }
        return d.toString();
    }

    public static String ByteToHex(byte[] bytes) {
        StringBuffer sha1StrBuff = new StringBuffer();
        for (int i = 0; i < bytes.length; ++i)
            if (Integer.toHexString(0xFF & bytes[i]).length() == 1) {
                sha1StrBuff.append("0").append(Integer.toHexString(0xFF & bytes[i]));
            } else
                sha1StrBuff.append(Integer.toHexString(0xFF & bytes[i]));


        return sha1StrBuff.toString();
    }

    public static String Hex2Str(byte[] b, int len) {
        String str = "";
        char[] ch = new char[len * 2];

        for (int i = 0; i < len; ++i) {
            if (((b[i] >> 4 & 0xF) < 10) && ((b[i] >> 4 & 0xF) >= 0))
                ch[(i * 2)] = (char) ((b[i] >> 4 & 0xF) + 48);
            else {
                ch[(i * 2)] = (char) ((b[i] >> 4 & 0xF) + 65 - 10);
            }

            if (((b[i] & 0xF) < 10) && ((b[i] & 0xF) >= 0))
                ch[(i * 2 + 1)] = (char) ((b[i] & 0xF) + 48);
            else
                ch[(i * 2 + 1)] = (char) ((b[i] & 0xF) + 65 - 10);

        }

        str = new String(ch);
        return str;
    }

    public String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; ++n) {
            stmp = Integer.toHexString(b[n] & 0xFF);
            if (stmp.length() == 1)
                hs = new StringBuilder().append(hs).append("0").append(stmp).toString();
            else
                hs = new StringBuilder().append(hs).append(stmp).toString();

            if (n < b.length - 1)
                hs = new StringBuilder().append(hs).append(":").toString();
        }

        return hs.toUpperCase();
    }

    public String genmac(byte[] inputByte, byte[] inputkey)
            throws Exception {
        Mac mac;
        try {
            mac = Mac.getInstance("HmacMD5");
            SecretKey key = new SecretKeySpec(inputkey, "DES");
            mac.init(key);

            byte[] macCode = mac.doFinal(inputByte);
            String strMac = byte2hex(macCode);
            return strMac;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    public boolean checkmac(byte[] inputByte, byte[] inputkey, String inputmac)
            throws Exception {
        Mac mac;
        try {
            mac = Mac.getInstance("HmacMD5");
            SecretKey key = new SecretKeySpec(inputkey, "DES");
            mac.init(key);

            byte[] macCode = mac.doFinal(inputByte);
            String strMacCode = byte2hex(macCode);

            return (strMacCode.equals(inputmac));
        } catch (Exception ex) {
            throw ex;
        }
    }

    public static String fillString(String string, char filler, int totalLength, boolean atEnd) {
        byte[] tempbyte = string.getBytes();
        int currentLength = tempbyte.length;
        int delta = totalLength - currentLength;

        for (int i = 0; i < delta; ++i)
            if (atEnd)
                string = new StringBuilder().append(string).append(filler).toString();
            else
                string = new StringBuilder().append(filler).append(string).toString();


        return string;
    }

    public static byte[] encryptedPin(PublicKey publicKey, byte[] plainPin)
            throws Exception {
        Cipher cipher;
        try {
            cipher = CliperInstance.getInstance();
            cipher.init(1, publicKey);
            int blockSize = cipher.getBlockSize();
            int outputSize = cipher.getOutputSize(plainPin.length);
            int leavedSize = plainPin.length % blockSize;
            int blocksSize = (leavedSize != 0) ? plainPin.length / blockSize + 1 : plainPin.length / blockSize;

            byte[] raw = new byte[outputSize * blocksSize];
            int i = 0;
            while (plainPin.length - i * blockSize > 0) {
                if (plainPin.length - i * blockSize > blockSize) {
                    cipher.doFinal(plainPin, i * blockSize, blockSize, raw, i * outputSize);
                } else {
                    cipher.doFinal(plainPin, i * blockSize, plainPin.length - i * blockSize, raw, i * outputSize);
                }

                ++i;
            }
            return raw;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public byte[] encryptedData(PublicKey publicKey, byte[] plainData)
            throws Exception {
        Cipher cipher;
        try {
            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", new BouncyCastleProvider());

            cipher.init(1, publicKey);
            int blockSize = cipher.getBlockSize();
            int outputSize = cipher.getOutputSize(plainData.length);
            int leavedSize = plainData.length % blockSize;
            int blocksSize = (leavedSize != 0) ? plainData.length / blockSize + 1 : plainData.length / blockSize;

            byte[] raw = new byte[outputSize * blocksSize];
            int i = 0;
            while (plainData.length - i * blockSize > 0) {
                if (plainData.length - i * blockSize > blockSize) {
                    cipher.doFinal(plainData, i * blockSize, blockSize, raw, i * outputSize);
                } else {
                    cipher.doFinal(plainData, i * blockSize, plainData.length - i * blockSize, raw, i * outputSize);
                }

                ++i;
            }
            return raw;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public static byte[] decryptedPin(PrivateKey privateKey, byte[] cryptPin)
            throws Exception {
        byte[] pinBlock;
        try {
            pinBlock = base64Decode(cryptPin);

            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", new BouncyCastleProvider());

            cipher.init(2, privateKey);
            int blockSize = cipher.getBlockSize();
            int outputSize = cipher.getOutputSize(pinBlock.length);
            int leavedSize = pinBlock.length % blockSize;
            int blocksSize = (leavedSize != 0) ? pinBlock.length / blockSize + 1 : pinBlock.length / blockSize;

            byte[] pinData = new byte[outputSize * blocksSize];
            int i = 0;
            while (pinBlock.length - i * blockSize > 0) {
                if (pinBlock.length - i * blockSize > blockSize) {
                    cipher.doFinal(pinBlock, i * blockSize, blockSize, pinData, i * outputSize);
                } else {
                    cipher.doFinal(pinBlock, i * blockSize, pinBlock.length - i * blockSize, pinData, i * outputSize);
                }

                ++i;
            }
            return pinData;
        } catch (Exception e) {
            LogUtil.writeErrorLog("解密失败", e);
        }
        return null;
    }

    private static byte[] pin2PinBlock(String aPin) {
        int tTemp = 1;
        int tPinLen = aPin.length();

        byte[] tByte = new byte[8];
        try {
            int i;
            String a;
            tByte[0] = (byte) Integer.parseInt(Integer.toString(tPinLen), 10);
            if (tPinLen % 2 == 0)
                for (i = 0; i < tPinLen; ) {
                    a = aPin.substring(i, i + 2);
                    tByte[tTemp] = (byte) Integer.parseInt(a, 16);
                    if ((i == tPinLen - 2) &&
                            (tTemp < 7)) {
                        for (int x = tTemp + 1; x < 8; ++x)
                            tByte[x] = -1;

                    }

                    ++tTemp;
                    i += 2;
                }
            else
                for (i = 0; i < tPinLen - 1; ) {
                    a = aPin.substring(i, i + 2);
                    tByte[tTemp] = (byte) Integer.parseInt(a, 16);
                    if (i == tPinLen - 3) {
                        String b = new StringBuilder().append(aPin.substring(tPinLen - 1)).append("F").toString();
                        tByte[(tTemp + 1)] = (byte) Integer.parseInt(b, 16);
                        if (tTemp + 1 < 7)
                            for (int x = tTemp + 2; x < 8; ++x)
                                tByte[x] = -1;

                    }

                    ++tTemp;
                    i += 2;
                }
        } catch (Exception e) {
        }
        return tByte;
    }

    private static byte[] formatPan(String aPan) {
        int tPanLen = aPan.length();
        byte[] tByte = new byte[8];

        int temp = tPanLen - 13;
        try {
            tByte[0] = 0;
            tByte[1] = 0;
            for (int i = 2; i < 8; ++i) {
                String a = aPan.substring(temp, temp + 2);
                tByte[i] = (byte) Integer.parseInt(a, 16);
                temp += 2;
            }
        } catch (Exception e) {
        }
        return tByte;
    }

    public static byte[] pin2PinBlockWithCardNO(String aPin, String aCardNO) {
        byte[] tPinByte = pin2PinBlock(aPin);
        if (aCardNO.length() == 11)
            aCardNO = new StringBuilder().append("00").append(aCardNO).toString();
        else if (aCardNO.length() == 12)
            aCardNO = new StringBuilder().append("0").append(aCardNO).toString();

        byte[] tPanByte = formatPan(aCardNO);
        byte[] tByte = new byte[8];
        for (int i = 0; i < 8; ++i)
            tByte[i] = (byte) (tPinByte[i] ^ tPanByte[i]);

        return tByte;
    }

    private static byte[] addPKCS1Padding(byte[] aBytesText, int aBlockSize) {
        if (aBytesText.length > aBlockSize - 3)
            return null;

        SecureRandom tRandom = new SecureRandom();
        byte[] tAfterPaddingBytes = new byte[aBlockSize];
        tRandom.nextBytes(tAfterPaddingBytes);
        tAfterPaddingBytes[0] = 0;
        tAfterPaddingBytes[1] = 2;
        int i = 2;
        for (; i < aBlockSize - 1 - aBytesText.length; ++i)
            if (tAfterPaddingBytes[i] == 0)
                tAfterPaddingBytes[i] = (byte) tRandom.nextInt();


        tAfterPaddingBytes[i] = 0;
        System.arraycopy(aBytesText, 0, tAfterPaddingBytes, i + 1, aBytesText.length);

        return tAfterPaddingBytes;
    }

    public String assymEncrypt(String tPIN, String iPan, RSAPublicKey publicKey) {
        System.out.println(new StringBuilder().append("SampleHashMap::assymEncrypt([").append(tPIN).append("])").toString());
        System.out.println(new StringBuilder().append("SampleHashMap::assymEncrypt(PIN =[").append(tPIN).append("])").toString());
        try {
            byte[] tTempBytes;
            int tKeyLength = 1024;
            int tBlockSize = tKeyLength / 8;

            byte[] tTemp = null;

            tTemp = pin2PinBlockWithCardNO(tPIN, iPan);
            tTemp = addPKCS1Padding(tTemp, tBlockSize);

            BigInteger tPlainText = new BigInteger(tTemp);
            BigInteger tCipherText = tPlainText.modPow(publicKey.getPublicExponent(), publicKey.getModulus());

            byte[] tCipherBytes = tCipherText.toByteArray();
            int tCipherLength = tCipherBytes.length;
            if (tCipherLength > tBlockSize) {
                tTempBytes = new byte[tBlockSize];
                System.arraycopy(tCipherBytes, tCipherLength - tBlockSize, tTempBytes, 0, tBlockSize);

                tCipherBytes = tTempBytes;
            } else if (tCipherLength < tBlockSize) {
                tTempBytes = new byte[tBlockSize];
                for (int i = 0; i < tBlockSize - tCipherLength; ++i)
                    tTempBytes[i] = 0;

                System.arraycopy(tCipherBytes, 0, tTempBytes, tBlockSize - tCipherLength, tCipherLength);

                tCipherBytes = tTempBytes;
            }
            String tEncryptPIN = new String(base64Encode(tCipherBytes));

            System.out.println(new StringBuilder().append("SampleHashMap::assymEncrypt(EncryptCardNo =[").append(tEncryptPIN).append("])").toString());

            return tEncryptPIN;
        } catch (Exception e) {
            e.printStackTrace(System.out);
            return tPIN;
        } catch (Error e) {
            e.printStackTrace(System.out);
        }
        return tPIN;
    }

    public static String trace(byte[] inBytes) {
        int j = 0;
        byte[] temp = new byte[76];
        bytesSet(temp, ' ');
        StringBuffer strc = new StringBuffer("");
        strc.append("----------------------------------------------------------------------------\n");

        for (int i = 0; i < inBytes.length; ++i) {
            if (j == 0) {
                System.arraycopy(String.format("%03d: ", new Object[]{Integer.valueOf(i)}).getBytes(), 0, temp, 0, 5);

                System.arraycopy(String.format(":%03d", new Object[]{Integer.valueOf(i + 15)}).getBytes(), 0, temp, 72, 4);
            }

            System.arraycopy(String.format("%02X ", new Object[]{Byte.valueOf(inBytes[i])}).getBytes(), 0, temp, j * 3 + 5 + ((j > 7) ? 1 : 0), 3);

            if (inBytes[i] == 0)
                temp[(j + 55 + ((j > 7) ? 1 : 0))] = 46;
            else
                temp[(j + 55 + ((j > 7) ? 1 : 0))] = inBytes[i];

            ++j;
            if (j == 16) {
                strc.append(new String(temp)).append("\n");
                bytesSet(temp, ' ');
                j = 0;
            }
        }
        if (j != 0) {
            strc.append(new String(temp)).append("\n");
            bytesSet(temp, ' ');
        }
        strc.append("----------------------------------------------------------------------------\n");

        return strc.toString();
    }

    private static void bytesSet(byte[] inBytes, char fill) {
        if (inBytes.length == 0)
            return;

        for (int i = 0; i < inBytes.length; ++i)
            inBytes[i] = (byte) fill;
    }
}