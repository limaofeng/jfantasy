package org.jfantasy.framework.crypto;

import com.sun.crypto.provider.SunJCE;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.Security;

public class DESPlus {

    private static final Log LOG = LogFactory.getLog(DESPlus.class);

    private static final String strDefaultKey = "jfantasy.org";
    private Cipher encryptCipher = null;
    private Cipher decryptCipher = null;

    public static String byteArr2HexStr(byte[] arrB) {
        int iLen = arrB.length;

        StringBuilder sb = new StringBuilder(iLen * 2);
        for (byte anArrB : arrB) {
            int intTmp = anArrB;

            while (intTmp < 0) {
                intTmp += 256;
            }

            if (intTmp < 16) {
                sb.append("0");
            }
            sb.append(Integer.toString(intTmp, 16));
        }
        return sb.toString();
    }

    public static byte[] hexStr2ByteArr(String strIn) {
        byte[] arrB = strIn.getBytes();
        int iLen = arrB.length;

        byte[] arrOut = new byte[iLen / 2];
        for (int i = 0; i < iLen; i += 2) {
            String strTmp = new String(arrB, i, 2);
            arrOut[(i / 2)] = (byte) Integer.parseInt(strTmp, 16);
        }
        return arrOut;
    }

    public DESPlus() {
        this(strDefaultKey);
    }

    public DESPlus(String strKey) {
        Security.addProvider(new SunJCE());
        try {
            Key key = getKey(strKey.getBytes());
            this.encryptCipher = Cipher.getInstance("DES");
            this.encryptCipher.init(1, key);

            this.decryptCipher = Cipher.getInstance("DES");
            this.decryptCipher.init(2, key);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }


    }

    public byte[] encrypt(byte[] arrB) throws BadPaddingException, IllegalBlockSizeException {
        return this.encryptCipher.doFinal(arrB);
    }

    public String encrypt(String strIn) {
        try {
            return byteArr2HexStr(encrypt(strIn.getBytes()));
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return "";
        }
    }

    public byte[] decrypt(byte[] arrB) throws BadPaddingException, IllegalBlockSizeException {
        return this.decryptCipher.doFinal(arrB);
    }

    public String decrypt(String strIn) {
        try {
            return new String(decrypt(hexStr2ByteArr(strIn)));
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return "";
        }
    }

    private Key getKey(byte[] arrBTmp) throws Exception {
        byte[] arrB = new byte[8];
        for (int i = 0; (i < arrBTmp.length) && (i < arrB.length); i++) {
            arrB[i] = arrBTmp[i];
        }
        return new SecretKeySpec(arrB, "DES");
    }

}