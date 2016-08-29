package org.jfantasy.pay.boot;


import org.apache.commons.io.output.ByteArrayOutputStream;
import org.hibernate.criterion.Restrictions;
import org.jfantasy.framework.util.common.StreamUtil;
import org.jfantasy.framework.util.common.file.FileUtil;
import org.jfantasy.pay.bean.PayConfig;
import org.jfantasy.pay.service.PayConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;

//@Component
public class PayConfigConfiguration implements CommandLineRunner {

    @Autowired
    private PayConfigService payConfigService;

    @Override
    public void run(String... args) throws Exception {
        //添加支付配置
        // 银联支付 1
        try {
            //保存支付配置
            PayConfig payConfig = payConfigService.findUnique(Restrictions.eq("payProductId", "chinapay"), Restrictions.eq("bargainorId", "739211601040001"));
            if (payConfig == null) {
                payConfig = new PayConfig();
            }
            payConfig.setName("正本上工银联电子支付 - 银联在线");
            payConfig.setPayConfigType(PayConfig.PayConfigType.online);
            payConfig.setPayProductId("chinapay");
            payConfig.setPayFeeType(PayConfig.PayFeeType.fixed);
            payConfig.setPayFee(BigDecimal.ZERO);
            payConfig.setBargainorId("739211601040001");
            payConfig.setBargainorKey("Zbsg2307");

            //签名证书
            File signCert = new File("/certs/trade.pfx");
            //验签证书
            File validateCert = new File("/certs/cp.cer");

            ByteArrayOutputStream output = new ByteArrayOutputStream();
            StreamUtil.copyThenClose(new FileInputStream(signCert), output);

            payConfig.set("signCert", output.toByteArray());

            output = new ByteArrayOutputStream();
            StreamUtil.copyThenClose(new FileInputStream(validateCert), output);
            payConfig.set("validateCert", output.toByteArray());

            payConfigService.save(payConfig);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        // 银联支付 2
        try {
            //签名证书
            File signCert = new File("/certs/tradebc.pfx");
            //验签证书
            File validateCert = new File("/certs/cp.cer");
            //保存支付配置
            PayConfig payConfig = payConfigService.findUnique(Restrictions.eq("payProductId", "chinapay"), Restrictions.eq("bargainorId", "739211601120001"));
            if (payConfig == null) {
                payConfig = new PayConfig();
            }
            payConfig.setName("正本上工银联电子支付 - 网银支付");
            payConfig.setPayConfigType(PayConfig.PayConfigType.online);
            payConfig.setPayProductId("chinapay");
            payConfig.setPayFeeType(PayConfig.PayFeeType.fixed);
            payConfig.setPayFee(BigDecimal.ZERO);
            payConfig.setBargainorId("739211601120001");
            payConfig.setBargainorKey("Zbsg2307");

            ByteArrayOutputStream output = new ByteArrayOutputStream();
            StreamUtil.copyThenClose(new FileInputStream(signCert), output);

            payConfig.set("signCert", output.toByteArray());

            output = new ByteArrayOutputStream();
            StreamUtil.copyThenClose(new FileInputStream(validateCert), output);
            payConfig.set("validateCert",output.toByteArray());

            payConfigService.save(payConfig);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        // 银联支付 主要给 app 使用
        try {
            //上传签名证书
            File signCert = new File("/certs/app.pfx");
            //上传验签证书
            File validateCert = new File("/certs/acp20151027.cer");
            //保存支付配置
            PayConfig payConfig = payConfigService.findUnique(Restrictions.eq("payProductId", "unionpay"), Restrictions.eq("bargainorId", "802310073920500"));
            if (payConfig == null) {
                payConfig = new PayConfig();
            }
            payConfig.setName("正本上工银联支付");
            payConfig.setPayConfigType(PayConfig.PayConfigType.online);
            payConfig.setPayProductId("unionpay");
            payConfig.setPayFeeType(PayConfig.PayFeeType.fixed);
            payConfig.setPayFee(BigDecimal.ZERO);
            payConfig.setBargainorId("802310073920500");
            payConfig.setBargainorKey("201601");

            ByteArrayOutputStream output = new ByteArrayOutputStream();
            StreamUtil.copyThenClose(new FileInputStream(signCert), output);

            payConfig.set("signCert", output.toByteArray());

            output = new ByteArrayOutputStream();
            StreamUtil.copyThenClose(new FileInputStream(validateCert), output);
            payConfig.set("validateCert", output.toByteArray());

            payConfigService.save(payConfig);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        // 微信支付
        try {
            //上传签名证书
            File encryptCert = new File("/certs/apiclient_cert.p12");
            PayConfig payConfig = payConfigService.findUnique(Restrictions.eq("payProductId", "weixinpay"), Restrictions.eq("bargainorId", "1320955801"));
            if (payConfig == null) {
                payConfig = new PayConfig();
            }
            payConfig.setName("正本上工微信支付");
            payConfig.setPayConfigType(PayConfig.PayConfigType.online);
            payConfig.setPayProductId("weixinpay");
            payConfig.setPayFeeType(PayConfig.PayFeeType.fixed);
            payConfig.setPayFee(BigDecimal.ZERO);
            payConfig.setBargainorId("1320955801");
            payConfig.setBargainorKey("HLb7DyKR39xqmjjyFHArdqWrxWfEt8FV");

            ByteArrayOutputStream output = new ByteArrayOutputStream();
            StreamUtil.copyThenClose(new FileInputStream(encryptCert), output);

            payConfig.set("appid", "wx4f4c272139cbad14");//使用该字段存储appid
            payConfig.set("encryptCert", encryptCert);//双向认证时需要用到的证书
            payConfigService.save(payConfig);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        // 微信手机支付
        try {
            //上传签名证书
            File encryptCert = new File("/certs/apiclient_cert_app.p12");
            PayConfig payConfig = payConfigService.findUnique(Restrictions.eq("payProductId", "weixinpay"), Restrictions.eq("bargainorId", "1332956301"));
            if (payConfig == null) {
                payConfig = new PayConfig();
            }
            payConfig.setName("正本上工微信支付(APP)");
            payConfig.setPayConfigType(PayConfig.PayConfigType.online);
            payConfig.setPayProductId("weixinpay");
            payConfig.setPayFeeType(PayConfig.PayFeeType.fixed);
            payConfig.setPayFee(BigDecimal.ZERO);
            payConfig.setBargainorId("1332956301");
            payConfig.setBargainorKey("GfolpZtqsx8BubiI5pS5JDhxqUZqLVqa");


            ByteArrayOutputStream output = new ByteArrayOutputStream();
            StreamUtil.copyThenClose(new FileInputStream(encryptCert), output);

            payConfig.set("appid", "wx958ba7d1684203b3");//appid
            payConfig.set("encryptCert", encryptCert);//双向认证时需要用到的证书

            payConfigService.save(payConfig);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        // 支付宝支付
        PayConfig payConfig = payConfigService.findUnique(Restrictions.eq("payProductId", "alipay"), Restrictions.eq("bargainorId", "2088021598024164"));
        if (payConfig == null) {
            payConfig = new PayConfig();
        }
        payConfig.setName("正本上工支付宝支付");
        payConfig.setPayConfigType(PayConfig.PayConfigType.online);
        payConfig.setPayProductId("alipay");
        payConfig.setPayFeeType(PayConfig.PayFeeType.fixed);
        payConfig.setPayFee(BigDecimal.ZERO);
        payConfig.setBargainorId("2088021598024164");
        payConfig.setBargainorKey("2s6pd34rf1u95t1hjlry13o1u13qxlbs");
        payConfig.set("sellerEmail", "shzbsg@126.com");
        //一些高级接口需要使用 RSA 或者 DSA 加密
        payConfig.set("rsaPrivateKey", FileUtil.readFile(new File("/certs/rsa_private_key_pkcs8.pem")));//RSA 加密时的私钥
        payConfig.set("rsaPublicKey", FileUtil.readFile(new File("/certs/rsa_public_key.pem")));//RSA 支付宝公钥
        payConfigService.save(payConfig);
    }

}
