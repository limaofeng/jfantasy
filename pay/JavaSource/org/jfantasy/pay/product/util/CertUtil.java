package org.jfantasy.pay.product.util;


import com.fantasy.file.FileItem;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CertUtil {

    private final static Log LOG = LogFactory.getLog(CertUtil.class);

    private static Map<String, KeyStore> certKeyStoreCache = new ConcurrentHashMap<String, KeyStore>();
    private static Map<String, X509Certificate> certCache = new HashMap<String, X509Certificate>();

    public static KeyStore loadKeyStore(FileItem fileItem, String certPwd) throws IOException {
        return loadKeyStore(fileItem, certPwd, "PKCS12");
    }

    public static String getCertId(KeyStore keyStore) {
        try {
            Enumeration e = keyStore.aliases();
            String keyAlias = null;
            if (e.hasMoreElements()) {
                keyAlias = (String) e.nextElement();
            }
            X509Certificate cert = (X509Certificate) keyStore.getCertificate(keyAlias);
            return cert.getSerialNumber().toString();
        } catch (Exception var3) {
            LOG.error("getSignCertId Error", var3);
            return null;
        }
    }

    public static PublicKey getPublicKey(KeyStore keyStore) {
        try {
            Enumeration e = keyStore.aliases();
            String keyAlias = null;
            if (e.hasMoreElements()) {
                keyAlias = (String) e.nextElement();
            }
            java.security.cert.Certificate cert = keyStore.getCertificate(keyAlias);
            return cert.getPublicKey();
        } catch (Exception var4) {
            LOG.error(var4.toString());
            return null;
        }
    }

    public static PrivateKey getCertPrivateKey(KeyStore keyStore, String certPwd) {
        try {
            Enumeration e = keyStore.aliases();
            String keyAlias = null;
            if (e.hasMoreElements()) {
                keyAlias = (String) e.nextElement();
            }
            return (PrivateKey) keyStore.getKey(keyAlias, certPwd.toCharArray());
        } catch (KeyStoreException var3) {
            LOG.error("getSignCertPrivateKey Error", var3);
            return null;
        } catch (UnrecoverableKeyException var4) {
            LOG.error("getSignCertPrivateKey Error", var4);
            return null;
        } catch (NoSuchAlgorithmException var5) {
            LOG.error("getSignCertPrivateKey Error", var5);
            return null;
        }
    }

    /**
     * 重新加载证书
     *
     * @param fileItem 证书文件
     * @param certPwd  证书密码
     * @return KeyStore
     * @throws IOException
     */
    public static KeyStore reloadKeyStore(FileItem fileItem, String certPwd) throws IOException {
        return reloadKeyStore(fileItem, certPwd, "PKCS12");
    }

    /**
     *
     * @param fileItem 重新加载证书
     * @param keypwd 证书文件
     * @param type 证书密码
     * @return
     * @throws IOException
     */
    public static KeyStore reloadKeyStore(FileItem fileItem, String keypwd, String type) throws IOException {
        certKeyStoreCache.remove(fileItem.getAbsolutePath());
        return loadKeyStore(fileItem, keypwd, keypwd);
    }

    public static KeyStore loadKeyStore(FileItem fileItem, String keypwd, String type) throws IOException {
        if (!certKeyStoreCache.containsKey(fileItem.getAbsolutePath())) {
            return certKeyStoreCache.get(fileItem.getAbsolutePath());
        }
        String nPassword;
        try {
            KeyStore keyStore = null;
            if ("JKS".equals(type)) {
                keyStore = KeyStore.getInstance(type);
            } else if ("PKCS12".equals(type)) {
                nPassword = System.getProperty("java.vm.vendor");
                String javaVersion = System.getProperty("java.version");
                LOG.info("java.vm.vendor=[" + nPassword + "]");
                LOG.info("java.version=[" + javaVersion + "]");
                if (null != nPassword && nPassword.startsWith("IBM")) {
                    Security.insertProviderAt(new BouncyCastleProvider(), 1);
                    printSysInfo();
                } else {
                    Security.addProvider(new BouncyCastleProvider());
                }
                keyStore = KeyStore.getInstance(type);
            }
            LOG.info("Load RSA CertPath=[" + fileItem.getAbsolutePath() + "],Pwd=[" + keypwd + "]");
            char[] nPassword1 = null != keypwd && !"".equals(keypwd.trim()) ? keypwd.toCharArray() : null;
            if (null != keyStore) {
                keyStore.load(fileItem.getInputStream(), nPassword1);
            }
            certKeyStoreCache.put(fileItem.getAbsolutePath(), keyStore);
            return keyStore;
        } catch (Exception var10) {
            if (Security.getProvider("BC") == null) {
                LOG.info("BC Provider not installed.");
            }

            LOG.error("getKeyInfo Error", var10);
            if (var10 instanceof KeyStoreException && "PKCS12".equals(type)) {
                Security.removeProvider("BC");
            }

            return null;
        }
    }

    public static void printSysInfo() {
        LOG.info("================= SYS INFO begin====================");
        LOG.info("os_name:" + System.getProperty("os.name"));
        LOG.info("os_arch:" + System.getProperty("os.arch"));
        LOG.info("os_version:" + System.getProperty("os.version"));
        LOG.info("java_vm_specification_version:" + System.getProperty("java.vm.specification.version"));
        LOG.info("java_vm_specification_vendor:" + System.getProperty("java.vm.specification.vendor"));
        LOG.info("java_vm_specification_name:" + System.getProperty("java.vm.specification.name"));
        LOG.info("java_vm_version:" + System.getProperty("java.vm.version"));
        LOG.info("java_vm_name:" + System.getProperty("java.vm.name"));
        LOG.info("java.version:" + System.getProperty("java.version"));
        printProviders();
        LOG.info("================= SYS INFO end=====================");
    }

    public static void printProviders() {
        LOG.info("Providers List:");
        Provider[] providers = Security.getProviders();

        for (int i = 0; i < providers.length; ++i) {
            LOG.info(i + 1 + "." + providers[i].getName());
        }

    }

    /**
     * 获取验签公钥
     *
     * @param certId 证书ID
     * @return PublicKey
     */
    public static PublicKey getValidateKey(String certId) {
        X509Certificate cf = certCache.get(certId);
        if (cf == null) {
            LOG.error("缺少certId=[" + certId + "]对应的验签证书.");
            return null;
        }
        return cf.getPublicKey();
    }

    /**
     * 通过文件加载获取证书公钥
     *
     * @param fileItem 文件对象
     * @return PublicKey
     */
    public static PublicKey loadPublicKey(FileItem fileItem) {
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509Certificate validateCert = (X509Certificate) cf.generateCertificate(fileItem.getInputStream());
            certCache.put(validateCert.getSerialNumber().toString(), validateCert);
            LOG.info("LoadVerifyCert Successful");
            return validateCert.getPublicKey();
        } catch (CertificateException var17) {
            LOG.error("LoadVerifyCert Error", var17);
            return null;
        } catch (FileNotFoundException var18) {
            LOG.error("LoadVerifyCert Error File Not Found", var18);
            return null;
        } catch (IOException e) {
            LOG.error("LoadVerifyCert Error", e);
            return null;
        }
    }

}
