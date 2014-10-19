package com;

import com.fantasy.framework.util.common.MessageDigestUtil;
import com.fantasy.framework.util.web.WebUtil;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;

public class Cryptogram {

    private static byte[] defaultIV = {1, 2, 3, 4, 5, 6, 7, 8};

    private static byte chr2hex(String chr) {
        if (chr.equals("0")) {
            return 0x00;
        } else if (chr.equals("1")) {
            return 0x01;
        } else if (chr.equals("2")) {
            return 0x02;
        } else if (chr.equals("3")) {
            return 0x03;
        } else if (chr.equals("4")) {
            return 0x04;
        } else if (chr.equals("5")) {
            return 0x05;
        } else if (chr.equals("6")) {
            return 0x06;
        } else if (chr.equals("7")) {
            return 0x07;
        } else if (chr.equals("8")) {
            return 0x08;
        } else if (chr.equals("9")) {
            return 0x09;
        } else if (chr.equals("A")) {
            return 0x0a;
        } else if (chr.equals("B")) {
            return 0x0b;
        } else if (chr.equals("C")) {
            return 0x0c;
        } else if (chr.equals("D")) {
            return 0x0d;
        } else if (chr.equals("E")) {
            return 0x0e;
        } else if (chr.equals("F")) {
            return 0x0f;
        }
        return 0x00;
    }

    public static byte[] HexStringToByteArray(String s) {
        byte[] buf = new byte[s.length() / 2];
        for (int i = 0; i < buf.length; i++) {
            buf[i] = (byte) (chr2hex(s.substring(i * 2, i * 2 + 1)) * 0x10 + chr2hex(s
                    .substring(i * 2 + 1, i * 2 + 2)));
        }
        return buf;
    }

    /**
     * Encrypt the data by the key.
     *
     * @param OriSource OriSource
     * @param key       key
     * @return string
     * @throws Exception
     */
    public static String encryptByKey(String OriSource, String key) throws Exception {
        String strResult;
        try {
            Cipher c3des = Cipher.getInstance("DESede/CBC/PKCS5Padding");
            SecretKeySpec myKey = new SecretKeySpec(HexStringToByteArray(key), "DESede");
            IvParameterSpec ivspec = new IvParameterSpec(defaultIV);
            c3des.init(Cipher.ENCRYPT_MODE, myKey, ivspec);
            byte[] testSrc = OriSource.getBytes();
            byte[] encoded = c3des.doFinal(testSrc);
            strResult = Base64Encrypt.getBASE64_byte(encoded);
        } catch (Exception e) {
            strResult = "";
            System.out.println("Encrypt failure!!!");
        }
        return strResult;
    }

    /**
     * Decrypt the encrypted data with the key.
     *
     * @param encryptedData encryptedData
     * @param key           key
     * @return strResult
     * @throws Exception
     */
    public static String decryptByKey(String encryptedData, String key) throws Exception {
        String strResult;
        try {
            Cipher c3des = Cipher.getInstance("DESede/CBC/PKCS5Padding");
            SecretKeySpec myKey = new SecretKeySpec(HexStringToByteArray(key), "DESede");
            IvParameterSpec ivspec = new IvParameterSpec(defaultIV);
            c3des.init(Cipher.DECRYPT_MODE, myKey, ivspec);
            byte[] s = Base64Encrypt.getByteArrFromBase64(encryptedData);
            byte[] encoded = c3des.doFinal(s);
            strResult = new String(encoded);
        } catch (Exception e) {
            strResult = "";
            System.out.println("Decrypt failure!!!");
        }
        return strResult;
    }

    public static void main(String[] args) throws Exception {
        //http://api.kangfuquan.com/api/circle/dynamic.do?
        String msg = "ot=1411710088241&channel=d566e64262f1d5af&version=1.0&requestId=7aacce24-6776-43f9-af70-f4fbe4ee28f7&data=iBdJhanvgNZp2mQwe5mlGJR6Udrr2vCjdtEtbvhGZnt0sGVYtjIYms9yEDYEnFefSOExIbnD1Ty%2F%0Amqal0pPrc7DvGDMIlLQlIj2pDmp%2FgdNPUVVC7PolbF0bbrlsf4ZS8h%2BFMy5%2FwTG8igqE6AjBNlLU%0AFhwmvk9IaZiq8QglGnXcc8jfMnTzvCgHv6rHrk1ARcA%2BDGnOLdkH9UM9R2BGtCIJMoQOQm9xJH5I%0AZeeMRYPzt%2F%2BgTincihtOkbI2VG4A&sign=41dacb826cd24c31ff4c6b70bb0617b8";
        String API_KEY_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCNYbfvYsg8qTG5RsWEjlHhfKZGYAOLPX1VxQjk9PTVXYByydqPXSlDl9IXvytyegEymJbE/O1D7E7VIlAZS7Em5fykfavCTPtA9z+2rd7iK+zGZ2dQfU/ivlMNWSiBdJF3a+J8R6vF+k7vtEdfCsIT7PqMOWP48Pcc4QvvLuraxwIDAQAB";
        String API_KEY_PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKLf3I2Oqoe3ezLmJPeizQaqDL7eJ9Dphx0r+92dbYxDqwvfYNiZkUwUT4kNNg0dTXsdRFGhEqQjEfZYlunx9iCvpEZ4m9ZYHVP8XwIJ2Ou0boSCaBlHpfWDhYTMAU27QeUPpXplr2AtU+dTi9f96zANSNzralhLwhlMazoVuSShAgMBAAECgYEAlBb/LiZ9nxCRD+J50j9QM7MCC7lqcmUi2L9ZLlMpe9M6/nyITeizV31QI8jDUIFIJZyitaXCkULh3h8GeIfrxKuOhIxpB95yZEmtmmamA58Wslt7gvZsPXxTJ7/hYO5HghJagAc6jgySnwNV38RquO8mvE65epmgMMbsHtTaYwECQQDyrrdQW2izVkRvYpSChwcrSFAQ7PQcqBCNI7nnUZhxq9NAABUf7oIwciTbjPIlGcxOQIqffrUdrlRPEoT5VA+5AkEAq8/507sOkCnX+addB7036TgAgHmoJCN14NjLyoJfG5kgfvqR2/h9CSG0oG1qkkxtEccxo7ErY7c8pnlimmegKQJAVstT9qJqpNEysp+QO8qtLrazJAqLGACnwsLHR0rweZ8Zc6dT5tK3rznzoq4bIFfEsSv2KCr5/b6OmqUl98IvyQJALw99b6hZ+dTsSn60Na13hhMH2Cj6jEOGQzs+vs5s/KM5ym4Zy7XJkdnAnvy5Zy815Nu51gtuRhbnrvWqs35U2QJBAJGMNJkpy5j3/Et7g3kygnl92x9cGFrPgccWT5N1byywhmTaD3qd/uWce99/Nm8We55XGDbhX+MXzSIGeiDCR74=";
        String API_KEY_SERVER = "b94fed3d0596c0b9b147b063b1765e1ec9bda583382db733";
        String API_KEY_CLIENT = "86A659D3035B51B1B66DF3139F1AEC33F6651334F1E65170";
        Map<String, String[]> params = WebUtil.parseQuery(msg);
        String version = params.get("version")[0];
        String ot = params.get("ot")[0];
        String data = params.get("data")[0];
        System.out.println("sign>" + params.get("sign")[0]);
        System.out.println("sign>" + MessageDigestUtil.getInstance().get(version + ot + data + API_KEY_CLIENT));
        String _msg = URLDecoder.decode(decryptByKey(data, API_KEY_CLIENT), "utf-8");
        System.out.println(_msg);
        System.out.println("-------------------------------------------------");
        System.out.println(data);
        System.out.println("-------------------------------------------------");
        String s =Cryptogram.encryptByKey(URLEncoder.encode(_msg,"utf-8"), API_KEY_CLIENT);
        System.out.println(s);
    }

}
