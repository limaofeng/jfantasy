package org.jfantasy.framework.crypto;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.DigestUtils;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class RSAUtilTest {

    private static final Log LOG = LogFactory.getLog(RSAUtilTest.class);

    @Test
    public void testGenerateKeyPair() throws Exception {

        KeyPair keyPair = RSAUtil.generateKeyPair();
        //公钥
        RSAPublicKey pubKey = (RSAPublicKey) keyPair.getPublic();

        //私钥
        RSAPrivateKey priKey = (RSAPrivateKey) keyPair.getPrivate();

        LOG.debug("pubKey = " + pubKey.toString());

        LOG.debug("priKey = " + priKey.toString());

    }

    @Test
    public void testEncrypt() throws Exception {
        RSAPublicKey recoveryPubKey = RSAUtil.getRSAPublicKey(pubKey);

        String source = DigestUtils.md5DigestAsHex("加密的数据".getBytes());
        LOG.debug("原始字符串:" + source);

        String enstr = RSAUtil.encrypt(recoveryPubKey, source);

        LOG.debug("加密后的:" + enstr +"\n长度:"+enstr.length());

        RSAPrivateKey recoveryPriKey = RSAUtil.getRSAPrivateKey(priKey);

        String destr = RSAUtil.decrypt(recoveryPriKey, enstr);
        LOG.debug("解码字符:" + destr);

        assert destr.equals(source);
    }

    private String pubKey;

    private String priKey;

    @Before
    public void setUp() throws Exception {
        pubKey = "RSA Public Key\n" +
                "            modulus: c3bdfbc49c74cf42af50b2b265c1ea88db13e20e5c5dbb22126aede5ec23fe4aa9eee378082437e69debbba775865925c54a7f660395aa31d64aa2549e365b0b7e46c0a09109900d00b1e298b245d9f0df25e59d0ab83731b837ac5916a4907cc730532d106dce1bcc951fd98694395a94e935af0fa11d9c992d90372b79d0c5\n" +
                "    public exponent: 10001";
        priKey = "RSA Private CRT Key\n" +
                "            modulus: c3bdfbc49c74cf42af50b2b265c1ea88db13e20e5c5dbb22126aede5ec23fe4aa9eee378082437e69debbba775865925c54a7f660395aa31d64aa2549e365b0b7e46c0a09109900d00b1e298b245d9f0df25e59d0ab83731b837ac5916a4907cc730532d106dce1bcc951fd98694395a94e935af0fa11d9c992d90372b79d0c5\n" +
                "    public exponent: 10001\n" +
                "   private exponent: aedc9f778a95473a39478bfb96afdf07173ef1e90656ad7fc89841a1ee8c50f4fe4dec632452c6a57c5a44af5a6c04639b95ebad84724532744f04293795334ac444198c46c5d67856d46623042cd0cb91b7a25412e8a1586872bef134e0a05e18da6cdf62339885b617d8ae85ac7f10e4362d3a14ea44f8094132cfa5b8cdd9\n" +
                "             primeP: fcb71810c8276ebfcfaca4e29dc960f7021dc9210789be00f62c914e9e11f22aeda85862d01618e40b1b073f102bba5b48deee6543b9ef7eff9d696892907d8f\n" +
                "             primeQ: c6494ff8748f9f3290b2fa9a4945fa02ba966c351e314c08bd964588c9cee4e1cf11ce5fab0c491ca6c9d083bb6761d7de28f7d1a74b39f6d04b55b8bd964a6b\n" +
                "     primeExponentP: 29d4a4c7225a649a9b6598656a78caf418059625417a2b593d66632700433832a2e41abd407467ea576c41e6ce666e926d9d791889fd5d07488865ca4978615\n" +
                "     primeExponentQ: c4daf3f20e86a7de6399cd026fcc7131545d128089c117637615426353377c345287b30377a572d06725e545634077fb002c5c1c60a266a18a99c038f0bfcc91\n" +
                "     crtCoefficient: 6dda38f5d57439dffa98987e9da7ced158704d67a19d1a66f6938b32e6992f6aace1072fd4732cc7d86e9cf243bb20783213ae2e9f20fa2eae4403a2e44e3d52\n";
    }

    @Test
    public void testDecrypt() throws Exception {

    }

}