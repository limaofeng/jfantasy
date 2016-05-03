package org.jfantasy.pay.service;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.Restrictions;
import org.jfantasy.framework.jackson.JSON;
import org.jfantasy.framework.util.common.PathUtil;
import org.jfantasy.framework.util.common.StreamUtil;
import org.jfantasy.framework.util.common.file.FileUtil;
import org.jfantasy.pay.ApplicationTest;
import org.jfantasy.pay.bean.PayConfig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(ApplicationTest.class)
public class PayConfigServiceTest {

    private final static Log LOG = LogFactory.getLog(PayConfigServiceTest.class);

    @Autowired
    private PayConfigService payConfigService;

    @Before
    public void setUp() throws Exception {
        Assert.assertNotNull(payConfigService);
    }

    @Test
    public void json() throws IOException {
        String resourcesPath = PathUtil.class.getResource("/application.properties").toExternalForm().replaceAll("^file:", "").replaceAll("/application\\.properties$", "");
        //上传签名证书
        File signCert = new File(resourcesPath + "/certs/app.pfx");
        //上传验签证书
        File validateCert = new File(resourcesPath + "/certs/acp20151027.cer");
        //保存支付配置
        PayConfig payConfig = new PayConfig();
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
        payConfig.set("validateCert", FileUtil.readFile(validateCert));

        String json = JSON.serialize(payConfig);

        LOG.debug(json);

        PayConfig config = JSON.deserialize(json, PayConfig.class);

        assert config != null;
        Assert.assertArrayEquals(config.get("signCert",byte[].class), payConfig.get("signCert", byte[].class));
    }

    @Test
    public void save() throws Exception {
        String resourcesPath = PathUtil.class.getResource("/application.properties").toExternalForm().replaceAll("^file:", "").replaceAll("/application\\.properties$", "");
        //上传签名证书
        File signCert = new File(resourcesPath + "/certs/app.pfx");
        //上传验签证书
        File validateCert = new File(resourcesPath + "/certs/acp20151027.cer");
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
        payConfig.set("validateCert", FileUtil.readFile(validateCert));
        payConfigService.save(payConfig);

    }

}